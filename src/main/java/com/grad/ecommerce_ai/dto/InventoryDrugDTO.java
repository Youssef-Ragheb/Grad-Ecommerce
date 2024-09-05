package com.grad.ecommerce_ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InventoryDrugDTO {
    private String id;
    @NotBlank
    private String drugId;
    @NotBlank
    private String categoryId;
    @NotBlank
    private String activeIngredientId;
    @NotBlank
    private float price;
    @NotBlank
    private int stock;
    @NotBlank
    private Long branchId;
}
