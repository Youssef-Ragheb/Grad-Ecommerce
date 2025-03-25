package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.LogHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface LogHistoryRepository extends MongoRepository<LogHistory, String> {
    Optional<LogHistory> findByUserId(Long userId);
}
