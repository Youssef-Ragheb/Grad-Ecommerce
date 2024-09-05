package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.CompanyDTO;
import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Company;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.CompanyDetails;
import com.grad.ecommerce_ai.repository.CompanyDetailsRepository;
import com.grad.ecommerce_ai.repository.CompanyRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.*;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CompanyDetailsService companyDetailsService;
    private final CompanyDetailsRepository companyDetailsRepository;
    private final CheckAuth checkAuth;

    public CompanyService(CompanyRepository companyRepository, JwtService jwtService, UserRepository userRepository, CompanyDetailsService companyDetailsService, CompanyDetailsRepository companyDetailsRepository, CheckAuth checkAuth) {
        this.companyRepository = companyRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.companyDetailsService = companyDetailsService;
        this.companyDetailsRepository = companyDetailsRepository;
        this.checkAuth = checkAuth;
    }

    public ApiResponse<List<CompanyDTO>> getAllCompanies() {
        ApiResponse<List<CompanyDTO>> apiResponse = new ApiResponse<>();
        List<Company> pharmacies = companyRepository.findAll();
        apiResponse.setData(companyListToDtoList(pharmacies));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Companies found");
        apiResponse.setStatus(true);
        return apiResponse;

    }

    public ApiResponse<CompanyDTO> getCompanyById(Long id) {
        ApiResponse<CompanyDTO> apiResponse = new ApiResponse<>();
        Optional<Company> pharmacy = companyRepository.findById(id);
        if(pharmacy.isPresent()) {
            apiResponse.setData(companyToDto(pharmacy.get()));
            apiResponse.setStatusCode(200);
            apiResponse.setMessage("Company found");
            apiResponse.setStatus(true);
            return apiResponse;
        }
        apiResponse.setStatusCode(404);
        apiResponse.setMessage("Company Not Found");
        apiResponse.setStatus(false);
        return apiResponse;
    }

    @Transactional
    public ApiResponse<CompanyDTO> createCompany(Company company, String token) {
        ApiResponse<CompanyDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        if(companyRepository.existsByName(company.getName())) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("Company name already exists");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if(companyRepository.existsByCompanyEmail(company.getCompanyEmail())){
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("Company email already exists");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        CompanyDetails companyDetails = new CompanyDetails();

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("User not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if (companyDetailsRepository.existsByUser(user.get())){
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("User already have company");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Set company details and mark user as registered
        companyDetails.setUser(user.get());
        Company savedCompany = companyRepository.save(company);
        companyDetails.setCompany(savedCompany);
        ApiResponse<CompanyDetails> companyDetailsApiResponse = companyDetailsService.createCompanyDetails(companyDetails);
        if(companyDetailsApiResponse.getData() == null){
            apiResponse.setStatusCode(companyDetailsApiResponse.getStatusCode());
            apiResponse.setMessage(companyDetailsApiResponse.getMessage());
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Mark the user as having completed registration
        User registeredUser = user.get();
        registeredUser.setCompanyRegistrationCompleted(true);
        userRepository.save(registeredUser);

        apiResponse.setData(companyToDto(savedCompany));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Company Created");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    @Transactional
    public ApiResponse<CompanyDTO> updateCompany(Long companyId, Company updatedCompany, String token) {
        ApiResponse<CompanyDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        Optional<Company> existingCompanyOpt = companyRepository.findById(companyId);
        if (existingCompanyOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Company not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Company existingCompany = existingCompanyOpt.get();

        Optional<Company> conflictingCompanyOpt = companyRepository.findByNameOrEmailOrPhone(
                updatedCompany.getName(), updatedCompany.getCompanyEmail(), updatedCompany.getPhone()
        );

        if (conflictingCompanyOpt.isPresent()) {
            Company conflictingCompany = conflictingCompanyOpt.get();

            if (!conflictingCompany.getCompanyId().equals(companyId)) {
                if (conflictingCompany.getName().equals(updatedCompany.getName())) {
                    apiResponse.setStatusCode(400);
                    apiResponse.setMessage("Company name already exists");
                    apiResponse.setStatus(false);
                    return apiResponse;
                } else if (conflictingCompany.getCompanyEmail().equals(updatedCompany.getCompanyEmail())) {
                    apiResponse.setStatusCode(400);
                    apiResponse.setMessage("Company email already exists");
                    apiResponse.setStatus(false);
                    return apiResponse;
                } else if (conflictingCompany.getPhone().equals(updatedCompany.getPhone())) {
                    apiResponse.setStatusCode(400);
                    apiResponse.setMessage("Company phone already exists");
                    apiResponse.setStatus(false);
                    return apiResponse;
                }
            }
        }

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setCompanyEmail(updatedCompany.getCompanyEmail());
        existingCompany.setPhone(updatedCompany.getPhone());
        existingCompany.setLogoUrl(
                updatedCompany.getLogoUrl() != null && !updatedCompany.getLogoUrl().isEmpty() ?
                        updatedCompany.getLogoUrl() :
                        existingCompany.getLogoUrl()
        );

        Company savedCompany = companyRepository.save(existingCompany);

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("User not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<CompanyDetails> companyDetails = companyDetailsRepository.findByUser(user.get());


        if (companyDetails.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("User do not have a company");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if(companyDetails.get().getCompany().getCompanyId().equals(companyId)){
            apiResponse.setStatusCode(401);
            apiResponse.setMessage("Unauthorized");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        companyDetails.get().setCompany(savedCompany);
        companyDetailsRepository.save(companyDetails.get());

        apiResponse.setData(companyToDto(savedCompany));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Company updated successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }


    @Transactional
    public ApiResponse<Boolean> deleteCompany(Long id, String token) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    apiResponse.setStatusCode(404);
                    apiResponse.setMessage("Company Not Found");
                    apiResponse.setStatus(false);
                    return new EntityNotFoundException("Company Not Found");
                });

        if (!company.getBranchList().isEmpty()) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("Branch already exists. Delete all branches to delete the company.");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Long userId = jwtService.extractUserId(token);
        User registeredUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    apiResponse.setStatusCode(404);
                    apiResponse.setMessage("User not found");
                    apiResponse.setStatus(false);
                    return new EntityNotFoundException("User Not Found");
                });

        CompanyDetails companyDetails = companyDetailsRepository.findByUser(registeredUser)
                .orElseThrow(() -> {
                    apiResponse.setStatusCode(404);
                    apiResponse.setMessage("User unauthorized or company details not found");
                    apiResponse.setStatus(false);
                    return new EntityNotFoundException("Company Details Not Found");
                });

        if (!companyDetails.getCompany().getCompanyId().equals(id)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("User is unauthorized to delete this company");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Update user's registration status
        registeredUser.setCompanyRegistrationCompleted(false);
        userRepository.save(registeredUser);

        // Delete company details
        companyDetailsRepository.delete(companyDetails);

        // Delete the company itself
        companyRepository.delete(company);

        apiResponse.setData(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Company deleted successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }
    public ApiResponse<List<BranchDTO>> getCompanyBranches(Long companyId) {
        ApiResponse<List<BranchDTO>> apiResponse = new ApiResponse<>();
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        if(companyOptional.isPresent()) {
            List<Branch> branchList  = companyOptional.get().getBranchList();
            apiResponse.setData(branchListToDtoList(branchList));
            apiResponse.setStatusCode(200);
            apiResponse.setMessage("Branches found");
            apiResponse.setStatus(true);
            return apiResponse;
        }
        apiResponse.setStatusCode(404);
        apiResponse.setMessage("Company Not Found");
        apiResponse.setStatus(false);
        return apiResponse;
    }
}
