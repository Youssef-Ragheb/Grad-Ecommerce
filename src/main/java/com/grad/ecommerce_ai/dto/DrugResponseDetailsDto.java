package com.grad.ecommerce_ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrugResponseDetailsDto {
    private String drugId;
    private String drugName;
    private String description;
    private boolean isAvailable;
    private String imageUrl;
    private float price;
}
