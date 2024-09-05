package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.ActiveIngredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveIngredientRepository extends MongoRepository<ActiveIngredient, String> {
    Optional<ActiveIngredient> findByActiveIngredient(String name);
}
