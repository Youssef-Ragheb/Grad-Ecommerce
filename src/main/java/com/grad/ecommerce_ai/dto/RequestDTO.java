package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.dto.enums.Status;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.entity.Order;
import com.grad.ecommerce_ai.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class RequestDTO {
    private Long requestId;
    private List<ItemDTO> items;
    private BranchDTO branch;
    private OrderDTO order;
    private Status status;
    private CustomerInfoDTO customer;
    private float totalPriceOfRequest;
}
