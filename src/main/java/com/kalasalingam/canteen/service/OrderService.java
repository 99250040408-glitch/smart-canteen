package com.kalasalingam.canteen.service;

import com.kalasalingam.canteen.dto.CartItemDTO;
import com.kalasalingam.canteen.entity.*;
import com.kalasalingam.canteen.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodItemRepository foodItemRepository;
    private final TokenRepository tokenRepository;

    public OrderService(OrderRepository orderRepository, FoodItemRepository foodItemRepository,
                        TokenRepository tokenRepository) {
        this.orderRepository = orderRepository;
        this.foodItemRepository = foodItemRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Order placeOrder(User user, List<CartItemDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        double total = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDTO cartItem : cartItems) {
            FoodItem foodItem = foodItemRepository.findById(cartItem.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found: " + cartItem.getFoodItemId()));

            if (!foodItem.getAvailable()) {
                throw new RuntimeException("Item not available: " + foodItem.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(foodItem.getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);

            total += orderItem.getPrice();
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);

        // Generate token
        Token token = new Token();
        token.setOrder(order);
        token.setTokenNo(generateTokenNumber());
        token.setCreatedAt(LocalDateTime.now());
        order.setToken(token);

        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Order updateStatus(Long orderId, String status) {
        Order order = findById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<Order> findActiveOrders() {
        return orderRepository.findByStatusInOrderByCreatedAtDesc(List.of("PLACED", "READY"));
    }

    public Map<String, Object> getDailySummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOrders", orderRepository.countOrdersSince(startOfDay));
        summary.put("totalRevenue", orderRepository.sumRevenueSince(startOfDay));
        summary.put("orders", orderRepository.findOrdersSince(startOfDay));
        return summary;
    }

    private synchronized String generateTokenNumber() {
        Long maxId = tokenRepository.findMaxId();
        long next = (maxId == null) ? 1 : maxId + 1;
        return String.format("T-%04d", next);
    }
}
