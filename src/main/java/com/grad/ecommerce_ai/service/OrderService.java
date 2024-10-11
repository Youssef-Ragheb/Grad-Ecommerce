package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Order;
import com.grad.ecommerce_ai.repository.OrderRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, JwtService jwtService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public ApiResponse<Order> saveOrder(Order order, String token) {
        ApiResponse<Order> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        order.setUserId(userId);

        Order savedOrder = orderRepository.save(order);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Order placed successfully");
        response.setData(savedOrder);

        return response;
    }

    public ApiResponse<Order> getOrderById(String id, String token) {
        ApiResponse<Order> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent() && orderOptional.get().getUserId().equals(userId)) {
            response.setStatus(true);
            response.setStatusCode(200);
            response.setMessage("Order found");
            response.setData(orderOptional.get());
        } else {
            response.setStatus(false);
            response.setStatusCode(404);
            response.setMessage("Order not found or unauthorized");
        }

        return response;
    }

    public ApiResponse<List<Order>> getUserOrders(String token) {
        ApiResponse<List<Order>> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        List<Order> userOrders = orderRepository.findAllByUserId(userId);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Orders retrieved");
        response.setData(userOrders);

        return response;
    }
}
