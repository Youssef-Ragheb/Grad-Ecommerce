package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.CompanyDetails;
import com.grad.ecommerce_ai.repository.CompanyDetailsRepository;
import com.grad.ecommerce_ai.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyDetailsService {
    private final CompanyDetailsRepository companyDetailsRepository;
    private final CompanyRepository companyRepository;

    public CompanyDetailsService(CompanyDetailsRepository companyDetailsRepository, CompanyRepository companyRepository) {
        this.companyDetailsRepository = companyDetailsRepository;
        this.companyRepository = companyRepository;
    }
    public ApiResponse<CompanyDetails> createCompanyDetails(CompanyDetails companyDetails) {
        ApiResponse<CompanyDetails> response = new ApiResponse<>();
        if(companyDetailsRepository.existsByUser(companyDetails.getUser())) {
            response.setStatusCode(500);
            response.setStatus(false);
            response.setMessage("User already exists");
            return response;
        }
        if(companyDetailsRepository.existsByCompany(companyDetails.getCompany())){
            response.setStatusCode(500);
            response.setStatus(false);
            response.setMessage("Company already exists");
            return response;
        }
        response.setStatusCode(200);
        response.setStatus(true);
        response.setData(companyDetailsRepository.save(companyDetails));
        response.setMessage("Company successfully created");
        return response;
    }
//    public ApiResponse<CompanyDetails> getCompanyDetailsByUser(User user) {
//        ApiResponse<CompanyDetails> apiResponse = new ApiResponse<>();
//
//        // Check if the user has associated CompanyDetails
//        if (user.getCompanyDetails() == null) {
//            apiResponse.setStatusCode(404);
//            apiResponse.setMessage("Company details not found for the user");
//            apiResponse.setStatus(false);
//            return apiResponse;
//        }
//
//        CompanyDetails companyDetails = user.getCompanyDetails();
//
//        apiResponse.setData(companyDetails);
//        apiResponse.setStatusCode(200);
//        apiResponse.setMessage("Company details retrieved successfully");
//        apiResponse.setStatus(true);
//        return apiResponse;
//    }
}
