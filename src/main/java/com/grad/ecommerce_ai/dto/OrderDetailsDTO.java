package com.grad.ecommerce_ai.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailsDTO {

    private String drugId;

    private String drugName;

    private float price;

    private int quantity;
    private LocalDateTime localDateTime;
}

