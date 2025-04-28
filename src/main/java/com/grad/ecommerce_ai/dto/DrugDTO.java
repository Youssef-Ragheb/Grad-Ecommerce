package com.grad.ecommerce_ai.dto;


import lombok.Data;

@Data
public class DrugDTO {

    private Long drugId;
    private ActiveIngredientDTO activeIngredientDto;
    private CategoryDTO categoryDTO;
    private String drugName;
    private String description;
    private String logoUrl;
}
