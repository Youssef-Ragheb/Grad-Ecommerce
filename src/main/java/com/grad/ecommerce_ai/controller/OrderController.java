package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Cart;
import com.grad.ecommerce_ai.entity.Order;
import com.grad.ecommerce_ai.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/save")
    public ApiResponse<Order> saveOrder(@RequestBody Order order, @RequestHeader String token) {
        return orderService.saveOrder(order, token);
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrder(@PathVariable String id, @RequestHeader String token) {
        return orderService.getOrderById(id, token);
    }

    @GetMapping("/user")
    public ApiResponse<List<Order>> getUserOrders(@RequestHeader String token) {
        return orderService.getUserOrders(token);
    }
    @PostMapping("/place/order")
    public ApiResponse<Order> placeOrder(@RequestBody Order order
            , @RequestHeader String token) {

        return orderService.createOrder(order, token);
    }
}
