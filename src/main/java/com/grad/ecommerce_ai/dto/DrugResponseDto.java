package com.grad.ecommerce_ai.dto;

import lombok.Data;

@Data
public class DrugResponseDto {
    private String drugId;
    private String drugName;
    private String description;
    private boolean isAvailable;
    private String imageUrl;
    private float price;
}
