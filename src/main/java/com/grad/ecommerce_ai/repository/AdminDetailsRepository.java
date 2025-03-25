package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdminDetailsRepository extends JpaRepository<AdminDetails, Integer> {
    Optional<AdminDetails> findByUser(User user);
}
