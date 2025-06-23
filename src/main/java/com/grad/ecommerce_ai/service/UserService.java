package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.TokenDTO;
import com.grad.ecommerce_ai.dto.UserCompanyDTO;
import com.grad.ecommerce_ai.dto.UserDTO;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Company;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.UserRoles;
import com.grad.ecommerce_ai.entity.details.AdminDetails;
import com.grad.ecommerce_ai.entity.details.CompanyDetails;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.*;
import com.grad.ecommerce_ai.utils.CheckAuth;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.UserMapper.dtoToUser;
import static com.grad.ecommerce_ai.mappers.UserMapper.userToDto;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdminDetailsRepository adminDetailsRepository;
    private final CheckAuth checkAuth;
    private final JwtService jwtService;
    private final BranchRepository branchRepository;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final CompanyRepository companyRepository;
    private final CompanyDetailsRepository companyDetailsRepository;

    public UserService(UserRepository userRepository, AdminDetailsRepository adminDetailsRepository, CheckAuth checkAuth, JwtService jwtService, BranchRepository branchRepository, EmployeeDetailsRepository employeeDetailsRepository, CompanyRepository companyRepository, CompanyDetailsRepository companyDetailsRepository) {
        this.userRepository = userRepository;
        this.adminDetailsRepository = adminDetailsRepository;
        this.checkAuth = checkAuth;
        this.jwtService = jwtService;
        this.branchRepository = branchRepository;
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.companyRepository = companyRepository;
        this.companyDetailsRepository = companyDetailsRepository;
    }

    //    public ApiResponse<Boolean> checkMailExists(String mail) {
//        ApiResponse<Boolean> response = new ApiResponse<>();
//        if(userRepository.existsByEmail(mail)){
//           response.setStatus(false);
//           response.setMessage("Email already exists");
//           response.setData(false);
//           response.setStatusCode(500);
//           return response;
//        }
//        response.setStatus(true);
//        response.setMessage("Email does not exist");
//        response.setData(true);
//        response.setStatusCode(200);
//        return response;
//    }
    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public ApiResponse<UserDTO> createClient(UserDTO userDTO) {
        ApiResponse<UserDTO> response = new ApiResponse<>();

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        userDTO.setEnabled(true);
        userDTO.setUserRoles(UserRoles.ROLE_CLIENT);
        userDTO.setPassword(encodePassword(userDTO.getPassword()));
        userDTO.setDateCreated(OffsetDateTime.now());

        User user = dtoToUser(userDTO);
        response.setStatusCode(200);
        response.setData(userToDto(userRepository.save(user)));
        response.setMessage("Successfully created client");
        response.setStatus(true);
        return response;
    }

    public ApiResponse<UserDTO> createAdmin(UserDTO userDTO, String password) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        if (!password.equals("hashPasswordAdmin")) {
            response.setData(null);
            response.setMessage("Can't create admin account ");
            response.setStatusCode(500);
            return response;
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        userDTO.setEnabled(true);
        userDTO.setUserRoles(UserRoles.ROLE_ADMIN);
        userDTO.setPassword(encodePassword(userDTO.getPassword()));
        userDTO.setDateCreated(OffsetDateTime.now());

        User user = dtoToUser(userDTO);
        response.setStatusCode(200);
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setUser(user);
        adminDetails.setPermissions("ROLE_ADMIN");
        adminDetailsRepository.save(adminDetails);
        response.setData(userToDto(userRepository.save(user)));
        response.setMessage("Successfully created client");
        response.setStatus(true);
        return response;
    }
    @Transactional
    public ApiResponse<UserCompanyDTO> createCompanyAccount(UserCompanyDTO userDTO) {
        ApiResponse<UserCompanyDTO> response = new ApiResponse<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        User user = new User();
        user.setEnabled(true);
        user.setUserRoles(UserRoles.ROLE_COMPANY);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setCompanyRegistrationCompleted(true);
        user.setDateCreated(OffsetDateTime.now());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setCity(userDTO.getCity());
        user.setGender(userDTO.getGender());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
       if(companyRepository.existsByCompanyEmailOrName
                (userDTO.getCompanyEmail(), userDTO.getCompanyName())){
           response.setStatus(false);
           response.setMessage("Company already exists with name or email");
           response.setData(null);
           response.setStatusCode(500);
       }
        Company company = new Company();
        company.setCompanyEmail(userDTO.getCompanyEmail());
        company.setName(userDTO.getCompanyName());
        if(companyRepository.existsByPhone(userDTO.getCompanyPhone())){
            response.setStatus(false);
            response.setMessage("Company already exists with phone number");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        company.setPhone(userDTO.getCompanyPhone());
        company.setLogoUrl(userDTO.getLogoUrl());
        company.setBranchList(new ArrayList<>());
        CompanyDetails companyDetails = new CompanyDetails();
        System.out.println(company);
        Company savedCompany = companyRepository.save(company);
        companyDetails.setCompany( savedCompany );
        User savedUser = userRepository.save(user);
        companyDetails.setUser(savedUser);
        companyDetailsRepository.save(companyDetails);
        response.setStatusCode(200);
        response.setData(userDTO);
        response.setMessage("Successfully created ");
        response.setStatus(true);
        return response;
    }

    public ApiResponse<UserDTO> createCompanyEmployee(UserDTO userDTO, Long branchId, String token) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        if (Objects.isNull(branchId)) {
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Invalid branch id");
            return response;
        }
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isEmpty()) {
            response.setStatus(false);
            response.setMessage("Branch not found");
            response.setStatusCode(500);
            return response;
        }
        if (!checkAuth.checkAuthToCompany(userId, branchOptional.get().getCompany().getCompanyId())) {
            response.setMessage("You are not authorized to access this company");
            response.setStatusCode(401);
            response.setStatus(false);
            return response;
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        userDTO.setEnabled(true);
        userDTO.setUserRoles(UserRoles.ROLE_EMPLOYEE);
        userDTO.setPassword(encodePassword(userDTO.getPassword()));
        userDTO.setDateCreated(OffsetDateTime.now());

        User user = dtoToUser(userDTO);
        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setUser(user);
        employeeDetails.setBranch(branchOptional.get());
        employeeDetailsRepository.save(employeeDetails);
        response.setStatusCode(200);
        response.setData(userToDto(userRepository.save(user)));
        response.setMessage("Successfully created client");
        response.setStatus(true);
        return response;
    }

    public ApiResponse<String> getUserRole(TokenDTO token){
        ApiResponse<String> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token.getToken());
        User user = userRepository.findById(userId).orElse(null);
        //assert user != null;
        response.setMessage("Role: "+user.getUserRoles());
        response.setStatus(true);
        response.setData(user.getUserRoles().toString());
        response.setStatusCode(200);
        return response;
    }

    public ApiResponse<UserDTO> getUserDetails(String token){
        ApiResponse<UserDTO> response = new ApiResponse<>();
        Long  userId = jwtService.extractUserId(token);
        User userOptional = userRepository.findById(userId).orElseThrow();
        UserDTO userDTO = userToDto(userOptional);
        response.setData(userDTO);
        response.setStatusCode(200);
        response.setMessage("User Details: ");
        response.setStatus(true);
        return response;
    }
    public ApiResponse<UserDTO> updateUserDetails(String token, UserDTO userDTO, String currentPassword) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            response.setStatus(false);
            response.setStatusCode(404);
            response.setMessage("User not found");
            return response;
        }

        // Check current password using BCrypt.matches instead of encoding & comparing
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(currentPassword, user.getPassword())) {
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Password does not match");
            return response;
        }

        // Update only non-null fields
        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        //if(userDTO.getPassword() != null) user.setPassword(encodePassword(userDTO.getPassword()));
        if (userDTO.getPhone() != null) user.setPhone(userDTO.getPhone());
        if (userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
        if (userDTO.getCity() != null) user.setCity(userDTO.getCity());
        if (userDTO.getGender() != null) user.setGender(userDTO.getGender());

        // You could allow updating password too (optional):
        // if (userDTO.getPassword() != null) user.setPassword(encoder.encode(userDTO.getPassword()));

        User updatedUser = userRepository.save(user);

        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Profile updated successfully");
        response.setData(userToDto(updatedUser));
        return response;
    }

    public ApiResponse<Boolean> checkPassword(String password,String token){
        ApiResponse<Boolean> response = new ApiResponse<>();
        Long  userId = jwtService.extractUserId(token);
        User userOptional = userRepository.findById(userId).orElseThrow();
        String encodedPassword = encodePassword(password);
        if(!encodedPassword.equals(userOptional.getPassword())){
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Password does not match");
            return response;
        }
        response.setData(true);
        response.setStatusCode(200);
        response.setMessage("Password Matched successfully");
        return response;
    }

    public ApiResponse<Boolean> deleteEmployee(String token, Long employeeId){
        ApiResponse<Boolean> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        User companyUser = userRepository.findById(userId).orElseThrow();
        Optional<CompanyDetails> companyDetails = companyDetailsRepository.findByUser(companyUser);
        if(companyDetails.isEmpty()){
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Company not found");
            return response;
        }
        User employeeUser = userRepository.findById(employeeId).orElseThrow();
        Optional<EmployeeDetails> employeeDetails = employeeDetailsRepository.findByUser(employeeUser);
        if(employeeDetails.isEmpty()){
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Employee not found");
            return response;
        }
        List<Branch> companyBranches = branchRepository.
                findBranchByCompany(companyDetails.get().getCompany()).orElseThrow();
        Long employeeBranchId = employeeDetails.get().getBranch().getBranchId();
        boolean haveAccess = false;
        for(Branch branch : companyBranches){
            if (branch.getBranchId().equals(employeeBranchId)) {
                haveAccess = true;
                break;
            }
        }
        if(!haveAccess){
            response.setStatus(false);
            response.setStatusCode(400);
            response.setMessage("Don't have access to delete this employee");
            return response;
        }
        employeeDetailsRepository.delete(employeeDetails.get());
       userRepository.delete(employeeUser);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Employee deleted successfully");
        return response;

    }
}
