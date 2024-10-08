package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.InventoryDrug;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDrugRepository extends MongoRepository<InventoryDrug, String> {
List<InventoryDrug> findAllByBranchId(Long id);
void deleteAllByBranchId(Long id);
List<InventoryDrug> findByCategoryId(String categoryId);
List<InventoryDrug> findAllByIdIn(List<String> ids);
List<InventoryDrug> findAllByDrugId(String id);
}
