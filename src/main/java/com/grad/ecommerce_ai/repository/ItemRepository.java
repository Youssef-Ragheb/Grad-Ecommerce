package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ItemRepository extends MongoRepository<Item, String> {
}
