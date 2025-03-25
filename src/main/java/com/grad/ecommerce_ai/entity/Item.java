package com.grad.ecommerce_ai.entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "item")
public class Item {
    @Id
    private String id;
    @Indexed
    private String orderId;
    @NotNull
    private String drugId;

    private float price;
    @NotNull
    private int quantity;
    @Indexed
    private Long userId;
}
