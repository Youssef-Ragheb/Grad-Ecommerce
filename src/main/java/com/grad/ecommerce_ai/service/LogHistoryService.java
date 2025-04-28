package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.LogHistory;
import com.grad.ecommerce_ai.repository.LogHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogHistoryService {

    private final LogHistoryRepository logHistoryRepository;

    public LogHistoryService(LogHistoryRepository logHistoryRepository) {
        this.logHistoryRepository = logHistoryRepository;
    }

//    public ApiResponse<LogHistory> addOrderToLog(String orderId, Long userId) {
//        ApiResponse<LogHistory> response = new ApiResponse<>();
//
//        Optional<LogHistory> logOptional = logHistoryRepository.findByUserId(userId);
//        LogHistory logHistory;
//
//        if (logOptional.isPresent()) {
//            logHistory = logOptional.get();
//            logHistory.getOrderIds().add(orderId);
//        } else {
//            logHistory = new LogHistory();
//            logHistory.setUser(user);
//            logHistory.setOrderIds(List.of(orderId));
//        }
//
//        logHistoryRepository.save(logHistory);
//        response.setStatus(true);
//        response.setStatusCode(200);
//        response.setMessage("Log updated");
//        response.setData(logHistory);
//
//        return response;
//    }

    public ApiResponse<LogHistory> getUserLogHistory(Long userId) {
        ApiResponse<LogHistory> response = new ApiResponse<>();

        Optional<LogHistory> logOptional = logHistoryRepository.findByUserId(userId);
        if (logOptional.isPresent()) {
            response.setStatus(true);
            response.setStatusCode(200);
            response.setMessage("Log history retrieved");
            response.setData(logOptional.get());
        } else {
            response.setStatus(false);
            response.setStatusCode(404);
            response.setMessage("No log history found");
        }

        return response;
    }
}
