package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(Long userId);
}
