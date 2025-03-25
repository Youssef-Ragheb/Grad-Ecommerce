package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByUserId(Long userId);
}
