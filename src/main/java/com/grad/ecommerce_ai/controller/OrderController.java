package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.OrderDTO;
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

    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getOrder(@PathVariable Long id, @RequestHeader String token) {
        return orderService.getOrderById(id, token);
    }

    @GetMapping("/user")
    public ApiResponse<List<OrderDTO>> getUserOrders(@RequestHeader String token) {
        return orderService.getUserOrders(token);
    }

    @PostMapping("/place/order")
    public ApiResponse<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO, @RequestHeader String token) {

        return orderService.createOrder(orderDTO, token);
    }
}
