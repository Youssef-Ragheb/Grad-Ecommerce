package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLocationRepository extends MongoRepository<UserLocation, String> {

    Optional<UserLocation> findByUserId(Long userId);

}
