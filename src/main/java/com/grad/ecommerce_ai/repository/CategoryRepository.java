package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByName(String name);
}
