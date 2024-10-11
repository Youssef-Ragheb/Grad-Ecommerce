package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.LogHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogHistoryRepository extends MongoRepository<LogHistory, String> {
    Optional<LogHistory> findByUserId(Long userId);
}
