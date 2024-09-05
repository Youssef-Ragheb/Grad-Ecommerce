package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    //TODO try to get with pharmacy ID
    Optional<List<Branch>> findBranchByCompany(Company company);
}
