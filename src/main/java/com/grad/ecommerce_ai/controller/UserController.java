package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.TokenDTO;
import com.grad.ecommerce_ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/role")
    public ResponseEntity<ApiResponse<String>> getUserRole(@RequestBody TokenDTO token){
        return new ResponseEntity<>(userService.getUserRole(token), HttpStatus.OK);

    }
}
