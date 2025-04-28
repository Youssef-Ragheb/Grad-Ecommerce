package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminDetailsRepository extends JpaRepository<AdminDetails, Long> {
    Optional<AdminDetails> findByUser(User user);
}
