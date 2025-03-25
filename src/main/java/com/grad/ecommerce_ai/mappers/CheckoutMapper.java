package com.grad.ecommerce_ai.mappers;



import com.grad.ecommerce_ai.dto.*;

import com.grad.ecommerce_ai.entity.*;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapper {

    // Request Mappings
    public RequestDTO toRequestDTO(Request request, User customer) {
        RequestDTO dto = new RequestDTO();
        dto.setItems(request.getItems().stream()
                .map(this::toItemDTO)
                .toList());
        dto.setBranchId(request.getBranchId());
        dto.setOrderId(request.getOrderId());
        dto.setCustomer(toCustomerInfoDTO(customer));
        dto.setId(request.getRequestId());
        return dto;
    }

    public Request toRequestEntity(RequestDTO dto) {
        Request request = new Request();
        request.setItems(dto.getItems().stream()
                .map(this::toItemEntity)
                .toList());
        request.setBranchId(dto.getBranchId());
        request.setOrderId(dto.getOrderId());
        request.setRequestId(dto.getId());
        return request;
    }

    // Item Mappings
    public ItemDTO toItemDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrderId());
        dto.setDrugId(item.getDrugId());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    public Item toItemEntity(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setOrderId(dto.getOrderId());
        item.setDrugId(dto.getDrugId());
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    // Customer Mappings
    public CustomerInfoDTO toCustomerInfoDTO(User user) {
        CustomerInfoDTO dto = new CustomerInfoDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        return dto;
    }

    public void updateUserFromDTO(CustomerInfoDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
    }
}