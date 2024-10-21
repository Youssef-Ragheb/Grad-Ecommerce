package com.grad.ecommerce_ai.repository;


import com.grad.ecommerce_ai.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);

}
