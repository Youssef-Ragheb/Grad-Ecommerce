package com.grad.ecommerce_ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveIngredientDto {
    private String id;
    @NotBlank
    private String ActiveIngredient;
    @NotBlank
    private String IngredientArabicName;
    @NotBlank
    private String description;
}
