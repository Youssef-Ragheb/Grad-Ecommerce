package com.grad.ecommerce_ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class  ApiResponse <T> {
    private int statusCode;
    private String message;
    private boolean status;
    private T data;
}
