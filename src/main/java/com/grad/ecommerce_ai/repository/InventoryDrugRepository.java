package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.InventoryDrug;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface InventoryDrugRepository extends MongoRepository<InventoryDrug, String> {
    List<InventoryDrug> findAllByBranchId(Long id);

    void deleteAllByBranchId(Long id);

    Optional<InventoryDrug> findByDrugIdAndBranchId(String id, Long branchId);

    List<InventoryDrug> findAllByDrugId(String id);

    List<InventoryDrug> findAllByIdIn(List<String> ids);

    List<InventoryDrug> findAllByDrugIdIn(List<String> ids);
    List<InventoryDrug> findAllByBranchIdAndDrugIdIn( Long branchId, List<String> ids);

    List<InventoryDrug> findByDrugId(String id);
}
