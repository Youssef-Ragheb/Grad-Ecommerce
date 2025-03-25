package com.grad.ecommerce_ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestDTO {
    private String id;
    private List<ItemDTO> items;
    private Long branchId;
    private String orderId;
    private CustomerInfoDTO customer;
}
