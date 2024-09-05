package com.grad.ecommerce_ai.controller;
import com.grad.ecommerce_ai.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to) {
        emailService.sendSimpleEmail(to, "Test Subject", "Test Email Content");
        return "Email sent successfully";
    }
}
