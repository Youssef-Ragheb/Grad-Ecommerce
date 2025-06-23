package com.grad.ecommerce_ai.dto;

import lombok.Data;

@Data
public class OrderDetailsDTO {

    private String drugId;

    private String drugName;

    private float price;

    private int quantity;
}

