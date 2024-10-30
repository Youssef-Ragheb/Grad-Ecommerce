package com.grad.ecommerce_ai.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "inventory_drug")
public class InventoryDrug {
    @Id
    private String id;
    @Indexed
    private String drugId;
    private String drugName;
    private String categoryId;
    private String activeIngredientId;
    private float price;
    private int stock;
    @Indexed
    private Long branchId;

}
