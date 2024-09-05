package com.grad.ecommerce_ai.utils;

import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Company;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.details.AdminDetails;
import com.grad.ecommerce_ai.enitity.details.CompanyDetails;
import com.grad.ecommerce_ai.enitity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.grad.ecommerce_ai.enitity.UserRoles.*;

@Service
public class CheckAuth {
    private final UserRepository userRepository;
    private final AdminDetailsRepository adminDetailsRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final CompanyDetailsRepository companyDetailsRepository;
    private final BranchRepository branchRepository;

    public CheckAuth(UserRepository userRepository, AdminDetailsRepository adminDetailsRepository, EmployeeDetailsRepository employeeDetails, CompanyDetailsRepository companyDetailsRepository, BranchRepository branchRepository) {
        this.userRepository = userRepository;
        this.adminDetailsRepository = adminDetailsRepository;
        this.employeeDetailsRepository = employeeDetails;
        this.companyDetailsRepository = companyDetailsRepository;
        this.branchRepository = branchRepository;
    }

    public boolean checkAuthToBranch(Long userId, Long branchId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }
        User userEntity = user.get();
        if (!userEntity.getUserRoles().equals(ROLE_ADMIN) && !userEntity.getUserRoles().equals(ROLE_COMPANY) && !userEntity.getUserRoles().equals(ROLE_EMPLOYEE)) {
            return false;
        }
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isEmpty()) {
            return false;
        }
        Branch branch = branchOptional.get();
        if (branch.getCompany() == null || branch.getCompany().getCompanyId() == null) {
            return false;
        }

        if (userEntity.getUserRoles().equals(ROLE_ADMIN)) {
            Optional<AdminDetails> admin  = adminDetailsRepository.findByUser(userEntity);
            return admin.isPresent();

        } else if (userEntity.getUserRoles().equals(ROLE_COMPANY)) {
            Optional<CompanyDetails> companyDetails = companyDetailsRepository.findByUser(userEntity);
            return companyDetails.filter(details -> branch.getCompany().getCompanyId().equals(details.getCompany().getCompanyId())).isPresent();

        } else if (userEntity.getUserRoles().equals(ROLE_EMPLOYEE)) {
            Optional<EmployeeDetails> employeeDetails = employeeDetailsRepository.findByUser(userEntity);
            return employeeDetails.filter(details -> branch.getEmployees().contains(details)).isPresent();
        } else {
            return false;
        }
    }
    public boolean checkAuthToCompany(Long userId, Long companyId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return false;
        }
        User userEntity = user.get();
        if (!userEntity.getUserRoles().equals(ROLE_ADMIN) && !userEntity.getUserRoles().equals(ROLE_COMPANY)) {
            return false;
        }

        if (userEntity.getUserRoles().equals(ROLE_ADMIN)) {
            Optional<AdminDetails> admin  = adminDetailsRepository.findByUser(userEntity);
            return admin.isPresent();

        } else if (userEntity.getUserRoles().equals(ROLE_COMPANY)) {
            Optional<CompanyDetails> companyDetails = companyDetailsRepository.findByUser(userEntity);
            if(companyDetails.isPresent()) {
                Company company = companyDetails.get().getCompany();
                return company.getCompanyId().equals(companyId);
            }
            return false;

        } return false;
    }
}
