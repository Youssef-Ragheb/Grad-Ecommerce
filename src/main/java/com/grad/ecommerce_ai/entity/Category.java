package com.grad.ecommerce_ai.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "category")
public class Category {
    @Id
    private String id;
    private String categoryName;
    private String logo;

}
