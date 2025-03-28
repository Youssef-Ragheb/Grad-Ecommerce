package com.grad.ecommerce_ai.entity;

import com.grad.ecommerce_ai.dto.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private float totalPrice;
    private String paymentMethod;
    private Status status;
    private Long userId;
    @Indexed
    private List<String> requestsIds;
}
