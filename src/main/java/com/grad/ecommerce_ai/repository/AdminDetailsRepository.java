package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdminDetailsRepository extends JpaRepository<AdminDetails, Integer> {
    Optional<AdminDetails> findByUser(User user);
}
