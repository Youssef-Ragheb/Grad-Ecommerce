package com.grad.ecommerce_ai.controller.request;

import com.grad.ecommerce_ai.service.ForgetPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auth")
public class ForgetPasswordController {

    private final ForgetPasswordService forgetPasswordService;

    public ForgetPasswordController(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendResetEmail(@RequestParam String email) {
        forgetPasswordService.sendResetEmail(email);
        return ResponseEntity.ok("Reset email sent if the user exists.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        forgetPasswordService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful.");
    }
}

