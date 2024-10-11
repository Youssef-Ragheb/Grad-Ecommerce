package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByUserId(Long userId);
}
