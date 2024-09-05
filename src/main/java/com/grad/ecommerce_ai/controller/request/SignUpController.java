package com.grad.ecommerce_ai.controller.request;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.UserDTO;
import com.grad.ecommerce_ai.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/client")
    public ResponseEntity<ApiResponse<UserDTO>> signUpForClient(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = userService.createClient(userDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/company")
    public ResponseEntity<ApiResponse<UserDTO>> signUpForCompany(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response = userService.createCompanyAccount(userDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/create/admin")
    public ResponseEntity<ApiResponse<UserDTO>> signUpForAdmin(@RequestBody UserDTO userDTO, @RequestParam String password) {
        ApiResponse<UserDTO> response = userService.createAdmin(userDTO,password);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/company/employee")
    public ResponseEntity<ApiResponse<UserDTO>> signUpForCompanyEmployee(@RequestBody UserDTO userDTO,@RequestParam Long branchId,@RequestHeader String token) {
        ApiResponse<UserDTO> response = userService.createCompanyEmployee(userDTO,branchId,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
