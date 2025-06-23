package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {
    Optional<EmployeeDetails> findByUser(User user);
    Long countByBranch_BranchIdIn(List<Long> branchIds);
    Boolean existsByUser(User user);
}
