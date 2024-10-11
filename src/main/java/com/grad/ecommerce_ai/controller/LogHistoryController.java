package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.LogHistory;
import com.grad.ecommerce_ai.service.JwtService;
import com.grad.ecommerce_ai.service.LogHistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log-history")
public class LogHistoryController {

    private final LogHistoryService logHistoryService;
    private final JwtService jwtService;

    public LogHistoryController(LogHistoryService logHistoryService, JwtService jwtService) {
        this.logHistoryService = logHistoryService;
        this.jwtService = jwtService;
    }

    @PostMapping("/add-order")
    public ApiResponse<LogHistory> addOrderToLog(@RequestParam String orderId, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token); // assuming JWT token service is in log service too
        return logHistoryService.addOrderToLog(orderId, userId);
    }

    @GetMapping("/user")
    public ApiResponse<LogHistory> getUserLogHistory(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token);
        return logHistoryService.getUserLogHistory(userId);
    }
}
