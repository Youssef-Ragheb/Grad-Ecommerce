package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.enitity.Company;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Integer> {
    Optional<CompanyDetails> findByUser(User user);
    Boolean existsByUser(User user);
    Boolean existsByCompany(Company company);
    CompanyDetails findByCompany(Company company);
    //CompanyDetails findByUser( User user);
}
