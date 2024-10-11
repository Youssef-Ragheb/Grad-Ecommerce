package com.grad.ecommerce_ai.controller.request;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.UserLogin;
import com.grad.ecommerce_ai.enitity.details.UserPrincipal;
import com.grad.ecommerce_ai.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.grad.ecommerce_ai.enitity.UserRoles.ROLE_COMPANY;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLogin user) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
                Long userId = userDetails.getUserId();
                String token = jwtService.generateToken(user.getEmail(), userId, userDetails.getUserRole());
                response.setStatusCode(200);
                response.setMessage("Successfully logged in");
                if ((!userDetails.getCompanyRegistration()) && userDetails.getUserRole().equals(ROLE_COMPANY)) {
                    response.setMessage("Need to complete company registration");
                }
                response.setStatus(true);
                response.setData(token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setStatusCode(401);
                response.setMessage("Login failed");
                response.setStatus(false);
                response.setData(null);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Login failed due to an exception: " + e.getMessage());
            response.setStatus(false);
            response.setData(null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
