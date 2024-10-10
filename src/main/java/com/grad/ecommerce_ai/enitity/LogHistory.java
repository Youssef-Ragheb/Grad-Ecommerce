package com.grad.ecommerce_ai.enitity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "log_history")
public class LogHistory {
    @Id
    private String id;
    private List<String> orderIds;
    @Indexed
    private Long userId;
}

