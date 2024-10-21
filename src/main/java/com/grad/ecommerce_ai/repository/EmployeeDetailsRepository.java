package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, Integer> {
    Optional<EmployeeDetails> findByUser(User user);
}
