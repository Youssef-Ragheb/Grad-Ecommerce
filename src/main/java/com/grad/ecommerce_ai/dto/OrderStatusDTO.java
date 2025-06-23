package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class OrderStatusDTO {
    private String orderId;
    private Status status;
    private LocalDateTime timestamp;
    private String message;
    private List<String> requestIds;

    public OrderStatusDTO(String orderId, Status status, LocalDateTime timestamp, String message, List<String> requestIds) {
        this.orderId = orderId;
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.requestIds = requestIds;
    }

    // Getters and setters
}