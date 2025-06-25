package com.grad.ecommerce_ai.mappers;



import com.grad.ecommerce_ai.dto.*;

import com.grad.ecommerce_ai.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckoutMapper {

    // Request Mappings
    public static RequestDTO toRequestDTO(Request request, User customer) {
        RequestDTO dto = new RequestDTO();
        List<ItemDTO> list = new ArrayList<>();
        for (Item item : request.getItems()) {
            ItemDTO itemDTO = toItemDTO(item);
            list.add(itemDTO);
        }
        dto.setItems(list);
        dto.setBranchId(request.getBranchId());
        dto.setOrderId(request.getOrderId());
        dto.setStatus(request.getStatus());
        dto.setCustomer(toCustomerInfoDTO(customer));
        dto.setId(request.getRequestId());
        dto.setRequestDate(request.getRequestDate());
        return dto;
    }

    public static Request toRequestEntity(RequestDTO dto) {
        Request request = new Request();
        List<Item> list = new ArrayList<>();
        for (ItemDTO itemDTO : dto.getItems()) {
            Item itemEntity = toItemEntity(itemDTO);
            list.add(itemEntity);
        }
        request.setItems(list);
        request.setBranchId(dto.getBranchId());
        request.setOrderId(dto.getOrderId());
        request.setStatus(dto.getStatus());
        request.setRequestId(dto.getId());
        request.setRequestDate(dto.getRequestDate());
        return request;
    }

    // Item Mappings
    public static ItemDTO toItemDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrderId());
        dto.setDrugId(item.getDrugId());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    public static Item toItemEntity(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setOrderId(dto.getOrderId());
        item.setDrugId(dto.getDrugId());
        item.setPrice(dto.getPrice());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    // Customer Mappings
    public static CustomerInfoDTO toCustomerInfoDTO(User user) {
        CustomerInfoDTO dto = new CustomerInfoDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        return dto;
    }

    public static void updateUserFromDTO(CustomerInfoDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
    }
}