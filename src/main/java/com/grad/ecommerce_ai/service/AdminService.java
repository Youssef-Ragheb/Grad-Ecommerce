package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    //display all users with there roles
    //change password to account of user
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        response.setData(userRepository.findAll());
        response.setStatus(true);
        response.setMessage("Users found");
        return response;
    }
    public ApiResponse<Boolean> changePassword(Long userId,String newPassword) {
        ApiResponse<Boolean> response = new ApiResponse<>();
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            response.setStatus(false);
            response.setMessage("User not found");
            return response;
        }

        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
        response.setData(true);
        response.setStatus(true);
        response.setMessage("Password changed successfully");
        return response;

    }
}
