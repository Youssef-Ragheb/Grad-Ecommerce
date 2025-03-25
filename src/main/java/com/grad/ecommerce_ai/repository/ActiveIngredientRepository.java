package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.ActiveIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ActiveIngredientRepository extends MongoRepository<ActiveIngredient, String> {
    List<ActiveIngredient> findByActiveIngredientContainingIgnoreCase(String name);
}
