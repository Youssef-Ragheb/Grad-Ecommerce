package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByCategoryName(String name);
}
