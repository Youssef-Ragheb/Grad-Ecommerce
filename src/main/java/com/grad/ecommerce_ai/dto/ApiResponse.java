package com.grad.ecommerce_ai.dto;

import lombok.Data;

@Data
public class  ApiResponse <T> {
    private int statusCode;
    private String message;
    private boolean status;
    private T data;
}
