package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Request;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface RequestRepository extends MongoRepository<Request, String> {

}
