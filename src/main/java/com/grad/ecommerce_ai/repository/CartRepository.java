package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(Long userId);
}
