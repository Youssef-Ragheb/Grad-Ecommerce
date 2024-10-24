package com.grad.ecommerce_ai.enitity;

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
    @NotNull// Optional, can link to Order (in MongoDB or MySQL)
    private String drugId;
    @NotNull// Relates to drug in MongoDB
    private Long branchId;// Relates to branch in MySQL
    private float price;
    @NotNull
    private int quantity;
    private Long userId;
}
