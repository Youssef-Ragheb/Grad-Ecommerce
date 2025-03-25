package com.grad.ecommerce_ai.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDTO {
    @Id
    private String id;

    private String orderId;
    @NotNull
    private String drugId;

    private float price;
    @NotNull
    private int quantity;

}
