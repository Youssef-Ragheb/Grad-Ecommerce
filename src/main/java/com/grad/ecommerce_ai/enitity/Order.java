package com.grad.ecommerce_ai.enitity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;                // Updated to long type
    private float totalPrice;
    private String paymentMethod;
    private String status;
    private Long userId;            // User ID to reference the MySQL User


}
