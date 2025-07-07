package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndDrugId(Long userId, String drugId);
}
