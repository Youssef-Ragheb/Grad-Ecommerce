package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.dto.enums.Status;
import com.grad.ecommerce_ai.entity.User;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    private float totalPrice;

    private String paymentMethod;

    private Status status;

    private User user;

    private List<RequestDTO> requests;

    private Timestamp createdAt;


}
