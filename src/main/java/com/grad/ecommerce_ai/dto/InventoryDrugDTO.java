package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Drugs;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InventoryDrugDTO {
    private Long id;
    @NotBlank
    private DrugDTO drugDTO;
    @NotBlank
    private float price;
    @NotBlank
    private int stock;
    @NotBlank
    private BranchDTO branchDTO;
}
