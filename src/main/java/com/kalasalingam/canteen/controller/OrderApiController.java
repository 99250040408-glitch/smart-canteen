package com.kalasalingam.canteen.controller;

import com.kalasalingam.canteen.entity.Order;
import com.kalasalingam.canteen.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}/status")
    public ResponseEntity<?> getOrderStatus(@PathVariable Long id) {
        try {
            Order order = orderService.findById(id);
            String tokenNo = order.getToken() != null ? order.getToken().getTokenNo() : "";
            return ResponseEntity.ok(Map.of(
                    "status", order.getStatus(),
                    "tokenNo", tokenNo
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
