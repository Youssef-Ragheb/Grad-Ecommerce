package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
