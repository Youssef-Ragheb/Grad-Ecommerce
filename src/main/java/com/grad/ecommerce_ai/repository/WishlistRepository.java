package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByUserId(Long userId);
}
