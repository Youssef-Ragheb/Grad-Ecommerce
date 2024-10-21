package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByUserId(Long userId);
}
