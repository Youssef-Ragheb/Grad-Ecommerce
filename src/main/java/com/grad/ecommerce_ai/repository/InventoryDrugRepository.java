package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

//edit
@Repository
public interface InventoryDrugRepository extends JpaRepository<InventoryDrug, Long> {
    List<InventoryDrug> findAllByBranch_BranchId(Long id);

    void deleteAllByBranch_BranchId(Long id);

    Optional<InventoryDrug> findByDrug_IdAndBranch_BranchId(Long id, Long branchId);

    List<InventoryDrug> findAllByDrug_Id(Long id);

    List<InventoryDrug> findAllByIdIn(List<Long> ids);

    List<InventoryDrug> findAllByDrugIdIn(List<Long> ids);

    Optional<InventoryDrug> findByDrugId(Long id);
}
