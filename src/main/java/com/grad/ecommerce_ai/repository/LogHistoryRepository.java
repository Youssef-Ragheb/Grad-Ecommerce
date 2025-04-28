package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.LogHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogHistoryRepository extends JpaRepository<LogHistory, Long> {
    Optional<LogHistory> findByUserId(Long userId);
}
