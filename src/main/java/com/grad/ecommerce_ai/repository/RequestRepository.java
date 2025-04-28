package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByBranch_BranchId(Long branchId);
}
