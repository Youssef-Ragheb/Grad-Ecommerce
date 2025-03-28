package com.grad.ecommerce_ai.entity;

import com.grad.ecommerce_ai.dto.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "request")
public class Request {
    @Id
    private String requestId;
    private List<Item> items;
    @Indexed
    private Long branchId;
    @Indexed
    private String orderId;
    private Status status;// "PENDING", "PREPARING", "READY", "SHIPPED"
    private Long customerId;
    private float totalPriceOfRequest;


}