package com.grad.ecommerce_ai.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user-location")
public class UserLocation {
    @Id
    private String id;
    private Long userId ;
    private double lat;
    private double lng;
}
