package com.kalasalingam.canteen.controller;

import com.kalasalingam.canteen.dto.CartItemDTO;
import com.kalasalingam.canteen.entity.FoodItem;
import com.kalasalingam.canteen.entity.Order;
import com.kalasalingam.canteen.entity.User;
import com.kalasalingam.canteen.service.FoodItemService;
import com.kalasalingam.canteen.service.OrderService;
import com.kalasalingam.canteen.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    private final FoodItemService foodItemService;
    private final OrderService orderService;
    private final UserService userService;

    public StudentController(FoodItemService foodItemService, OrderService orderService,
                             UserService userService) {
        this.foodItemService = foodItemService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/menu")
    public String menu(@RequestParam(required = false) String category,
                       @RequestParam(required = false) String search,
                       Model model) {
        List<FoodItem> items;
        if (search != null && !search.trim().isEmpty()) {
            items = foodItemService.search(search.trim());
        } else if (category != null && !category.isEmpty() && !category.equals("All")) {
            items = foodItemService.findByCategory(category);
        } else {
            items = foodItemService.findAvailable();
        }
        model.addAttribute("foodItems", items);
        model.addAttribute("categories", foodItemService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchQuery", search);
        return "menu";
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @PostMapping("/order/place")
    @ResponseBody
    public ResponseEntity<?> placeOrder(@RequestBody List<CartItemDTO> cartItems,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Order order = orderService.placeOrder(user, cartItems);
            return ResponseEntity.ok(Map.of("orderId", order.getId(), "tokenNo", order.getToken().getTokenNo()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/order/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        Order order = orderService.findById(id);
        // Ensure student can only view their own order
        if (!order.getUser().getEmail().equals(userDetails.getUsername())) {
            return "redirect:/menu";
        }
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @GetMapping("/my-orders")
    public String myOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderService.findByUser(user);
        model.addAttribute("orders", orders);
        return "my-orders";
    }
}
