package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestStatusDTO {
    private String requestId;
    private String orderId;
    private Status status;
    private LocalDateTime timestamp;
    private String message;
    // Add constructor, getters, setters
}
