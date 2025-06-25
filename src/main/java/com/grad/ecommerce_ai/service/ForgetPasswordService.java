package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ForgetPasswordService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> resetTokenRedisTemplate;
    private final JwtService jwtService;
    private final EmailService emailService;

    public ForgetPasswordService(
            UserRepository userRepository,
            RedisTemplate<String, String> resetTokenRedisTemplate,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.resetTokenRedisTemplate = resetTokenRedisTemplate;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }
    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    public void sendResetEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) throw new RuntimeException("User not found");


        String token = jwtService.generateResetToken(email, user.getId());
        System.out.println("generated token :"+token);
        System.out.println("The Id of the user: "+user.getId());

        // Store in Redis with 15 minutes TTL
        resetTokenRedisTemplate.opsForValue().set("reset_token:" + token, String.valueOf(user.getId()), 15, TimeUnit.MINUTES);
        System.out.println("Token stored with key: reset_token:" + token);
        // Construct the reset link
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        String subject = "Password Reset Request";
        String body = "Click the link to reset your password: " + resetLink;


        emailService.sendSimpleEmail(email, subject, body);
    }

    public Long validateResetToken(String token) {

        System.out.println(resetTokenRedisTemplate.opsForValue().get("reset_token:" + token));

        String userIdStr = resetTokenRedisTemplate.opsForValue().get("reset_token:" + token);
        if (userIdStr == null) throw new RuntimeException("Invalid or expired token");

        return Long.parseLong(userIdStr);
    }

    public void resetPassword(String token, String newPassword) {
        System.out.println(token);
        System.out.println(newPassword);
        Long userId = validateResetToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encodePassword(newPassword)); // You should encode this with a password encoder
        userRepository.save(user);

        // Invalidate the token
        resetTokenRedisTemplate.delete("reset_token:" + token);
    }
}

