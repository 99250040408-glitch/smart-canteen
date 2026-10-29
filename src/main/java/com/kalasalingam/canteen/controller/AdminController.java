package com.kalasalingam.canteen.controller;

import com.kalasalingam.canteen.entity.FoodItem;
import com.kalasalingam.canteen.entity.Order;
import com.kalasalingam.canteen.service.FoodItemService;
import com.kalasalingam.canteen.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final FoodItemService foodItemService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public AdminController(OrderService orderService, FoodItemService foodItemService) {
        this.orderService = orderService;
        this.foodItemService = foodItemService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Order> orders = orderService.findActiveOrders();
        model.addAttribute("orders", orders);
        return "admin/dashboard";
    }

    @PostMapping("/order/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     RedirectAttributes redirectAttributes) {
        orderService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Order status updated to " + status);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/menu")
    public String menuManagement(Model model) {
        model.addAttribute("foodItems", foodItemService.findAll());
        return "admin/menu-management";
    }

    @GetMapping("/menu/add")
    public String addItemForm(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("isEdit", false);
        return "admin/menu-form";
    }

    @GetMapping("/menu/edit/{id}")
    public String editItemForm(@PathVariable Long id, Model model) {
        FoodItem item = foodItemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        model.addAttribute("foodItem", item);
        model.addAttribute("isEdit", true);
        return "admin/menu-form";
    }

    @PostMapping("/menu/save")
    public String saveItem(@RequestParam String name,
                           @RequestParam Double price,
                           @RequestParam String category,
                           @RequestParam(required = false) Boolean available,
                           @RequestParam(required = false) String imageUrl,
                           @RequestParam(required = false) MultipartFile imageFile,
                           @RequestParam(required = false) Long id,
                           RedirectAttributes redirectAttributes) {
        try {
            FoodItem item;
            if (id != null) {
                item = foodItemService.findById(id)
                        .orElseThrow(() -> new RuntimeException("Item not found"));
            } else {
                item = new FoodItem();
            }

            item.setName(name);
            item.setPrice(price);
            item.setCategory(category);
            item.setAvailable(available != null ? available : false);

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(uniqueFilename);
                imageFile.transferTo(filePath.toFile());
                item.setImageUrl("/images/" + uniqueFilename);
            } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                item.setImageUrl(imageUrl.trim());
            }

            foodItemService.save(item);
            redirectAttributes.addFlashAttribute("success", "Menu item saved successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        foodItemService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Menu item deleted.");
        return "redirect:/admin/menu";
    }

    @PostMapping("/menu/toggle/{id}")
    public String toggleAvailability(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        FoodItem item = foodItemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setAvailable(!item.getAvailable());
        foodItemService.save(item);
        redirectAttributes.addFlashAttribute("success",
                item.getName() + " is now " + (item.getAvailable() ? "available" : "unavailable"));
        return "redirect:/admin/menu";
    }

    @GetMapping("/summary")
    public String dailySummary(Model model) {
        Map<String, Object> summary = orderService.getDailySummary();
        model.addAttribute("totalOrders", summary.get("totalOrders"));
        model.addAttribute("totalRevenue", summary.get("totalRevenue"));
        model.addAttribute("orders", summary.get("orders"));
        return "admin/daily-summary";
    }
}
