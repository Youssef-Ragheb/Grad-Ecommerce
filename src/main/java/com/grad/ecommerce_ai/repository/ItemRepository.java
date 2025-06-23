package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ItemRepository extends MongoRepository<Item, String> {
    List<Item> findByIdIn(List<String> id);
}
