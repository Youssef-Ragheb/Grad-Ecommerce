package com.grad.ecommerce_ai.controller;
import com.grad.ecommerce_ai.service.EmailService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/email")
@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to) {
        emailService.sendSimpleEmail(to, "Test Subject", "Test Email Content");
        return "Email sent successfully";
    }
    @GetMapping("/send-email/subscribe")
    public String sendSubscribe(@RequestParam String to) {
        emailService.sendSimpleEmail(to, "Curley Subscription", "You are now Subscribed too " +
                "our application ");
        return "Email sent successfully";
    }
}
