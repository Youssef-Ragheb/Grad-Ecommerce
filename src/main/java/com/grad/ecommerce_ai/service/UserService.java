package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.UserDTO;
import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.UserRoles;
import com.grad.ecommerce_ai.enitity.details.AdminDetails;
import com.grad.ecommerce_ai.enitity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.AdminDetailsRepository;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.EmployeeDetailsRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

    public UserService(UserRepository userRepository, AdminDetailsRepository adminDetailsRepository, CheckAuth checkAuth, JwtService jwtService, BranchRepository branchRepository, EmployeeDetailsRepository employeeDetailsRepository) {
        this.userRepository = userRepository;
        this.adminDetailsRepository = adminDetailsRepository;
        this.checkAuth = checkAuth;
        this.jwtService = jwtService;
        this.branchRepository = branchRepository;
        this.employeeDetailsRepository = employeeDetailsRepository;
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

    public ApiResponse<UserDTO> createCompanyAccount(UserDTO userDTO) {
        ApiResponse<UserDTO> response = new ApiResponse<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setData(null);
            response.setStatusCode(500);
            return response;
        }
        userDTO.setEnabled(true);
        userDTO.setUserRoles(UserRoles.ROLE_COMPANY);
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userDTO.setCompanyRegistrationCompleted(false);
        userDTO.setDateCreated(OffsetDateTime.now());
        User user = dtoToUser(userDTO);
        response.setStatusCode(200);
        response.setData(userToDto(userRepository.save(user)));
        response.setMessage("Successfully created client");
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

}
