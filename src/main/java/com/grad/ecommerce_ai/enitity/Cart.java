package com.grad.ecommerce_ai.enitity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private List<Item> items;
    @Indexed
    private Long userId;
}
