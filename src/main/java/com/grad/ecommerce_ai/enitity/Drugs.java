package com.grad.ecommerce_ai.enitity;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "drugs")
public class Drugs {
    @Indexed(unique = true)
    private String id;
    private String activeIngredientId;
    //TODO asm al company of drug
    private String drugName;
    private String drugArabicName;
    private String description;
    private String logo;
}
