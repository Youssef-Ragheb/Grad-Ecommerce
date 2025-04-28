package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Drugs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainDrugRepository extends JpaRepository<Drugs,Long> {
   // List<Drugs> findByActiveIngredientId(String id);
    List<Drugs> findByCategoryId(Long id);
    List<Drugs> DrugNameContainingIgnoreCase(String drugName);
    List<Drugs> findByDrugNameContainingIgnoreCase(String drugName);
}
