package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.TokenDTO;
import com.grad.ecommerce_ai.dto.UserDTO;
import com.grad.ecommerce_ai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/details")
    public ApiResponse<UserDTO> getUserDetails(@RequestHeader String token){
        return userService.getUserDetails(token);
    }
    @PostMapping("/password-check")
    public ResponseEntity<ApiResponse<Boolean>> checkPassword(@RequestBody String Password,@RequestHeader String token){
        ApiResponse<Boolean> response = userService.checkPassword(Password,token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/update-details")
    public ApiResponse<UserDTO> updateUserDetails(
            @RequestHeader String token,
            @RequestBody UserDTO userDTO,
            @RequestParam("currentPassword") String currentPassword
    ) {

        return userService.updateUserDetails(token, userDTO, currentPassword);
    }
    @DeleteMapping("/delete/employee/{employeeId}")
    public ApiResponse<Boolean> deleteEmployee(@RequestHeader String token,@PathVariable Long employeeId){
        return userService.deleteEmployee(token,employeeId);
    }
    @PostMapping("/employees/count")
    public Long getEmployeeCount(@RequestBody List<Long> employeeIds){
        return userService.getEmployeeCount(employeeIds);
    }

}
