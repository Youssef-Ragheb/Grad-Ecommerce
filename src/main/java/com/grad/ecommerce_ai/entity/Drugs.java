package com.grad.ecommerce_ai.entity;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "drugs")
public class Drugs {
    @Indexed(unique = true)
    private String id;
    @Indexed
    private String activeIngredientId;
    @Indexed
    private String categoryId;
    @Indexed
    private String drugName;
    private String description;
    @Size(max = 1080)
    private String logo;
}
