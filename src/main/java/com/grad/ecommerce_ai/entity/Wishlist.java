package com.grad.ecommerce_ai.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "wishlist")
public class Wishlist {
    @Id
    private String id;
    private String drugId;
    @Indexed
    private Long userId;
}

