package com.grad.ecommerce_ai.enitity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "active_ingredient")
public class ActiveIngredient {
    @Id
    private String id;
    @Field("activeIngredient")
    private String activeIngredient;
    @Field("ingredientArabicName")
    private String ingredientArabicName;
    private String description;
}
