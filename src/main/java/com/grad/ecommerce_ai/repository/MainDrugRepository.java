package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Drugs;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MainDrugRepository extends MongoRepository<Drugs,String> {
   // List<Drugs> findByActiveIngredientId(String id);
    List<Drugs> findByCategoryId(String id);
    List<Drugs> DrugNameContainingIgnoreCase(String drugName);
    List<Drugs> findByDrugNameContainingIgnoreCase(String drugName);
}
