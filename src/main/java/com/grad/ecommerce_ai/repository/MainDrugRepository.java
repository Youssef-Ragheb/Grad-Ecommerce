package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Drugs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainDrugRepository extends MongoRepository<Drugs,String> {
   // List<Drugs> findByActiveIngredientId(String id);
    List<Drugs> findByCategoryId(String id);
    List<Drugs> DrugNameContainingIgnoreCase(String drugName);
    List<Drugs> findByDrugNameContainingIgnoreCase(String drugName);
}
