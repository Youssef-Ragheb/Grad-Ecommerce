package com.grad.ecommerce_ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActiveIngredientDTO {
    private Long id;
    @NotBlank
    private String ActiveIngredient;
    @NotBlank
    private String IngredientArabicName;
    @NotBlank
    private String description;
}
