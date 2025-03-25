package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Boolean existsByName(String name);
    Boolean existsByCompanyEmailOrName(String email, String companyName);
    //Boolean existsByEmail(String email);
    Boolean existsByCompanyEmail(String email);
    //Boolean existsByPhone(String phone);
    @Query("SELECT c FROM Company c WHERE c.name = :name OR c.companyEmail = :email OR c.phone = :phone")
    Optional<Company> findByNameOrEmailOrPhone(@Param("name") String name, @Param("email") String email, @Param("phone") String phone);
    Boolean existsByPhone(String phone);
}
