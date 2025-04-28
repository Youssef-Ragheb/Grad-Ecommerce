package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Long> {
    Optional<EmployeeDetails> findByUser(User user);
}
