package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Drugs;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDTO {
    @Id
    private Long id;

    private DrugDTO drugDTO;

    private float unitPrice;
    @NotNull
    private int quantity;

}
