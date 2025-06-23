package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
      return adminService.getAllUsers();
    }
    @PostMapping("/change-Password")
    public ApiResponse<Boolean> changePassword(@RequestParam Long id,@RequestParam String password) {
        return adminService.changePassword(id, password);
    }
}
