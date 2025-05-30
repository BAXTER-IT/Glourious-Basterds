package com.crypto.orderbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderRequest) {
        try {
            // Extract order details
            String symbol = (String) orderRequest.get("symbol");
            String side = (String) orderRequest.get("side");
            Double price = Double.parseDouble(orderRequest.get("price").toString());
            Double quantity = Double.parseDouble(orderRequest.get("quantity").toString());
            
            // Log the order (in a real system, this would be saved to a database)
            System.out.println("Order received: " + side + " " + quantity + " " + symbol + " at " + price);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order placed successfully");
            response.put("orderId", "ORD" + System.currentTimeMillis()); // Generate a dummy order ID
            response.put("orderDetails", orderRequest);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to place order: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}