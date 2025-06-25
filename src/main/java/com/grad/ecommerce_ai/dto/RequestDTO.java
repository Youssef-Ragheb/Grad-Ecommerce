package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDTO {
    private String id;
    private List<ItemDTO> items;
    private Long branchId;
    private String orderId;
    private Status status;
    private CustomerInfoDTO customer;
    private LocalDateTime requestDate;
}
