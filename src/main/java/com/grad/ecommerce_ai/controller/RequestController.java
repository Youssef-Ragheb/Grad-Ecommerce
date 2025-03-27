package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/request")
public class RequestController {
    private final RequestService requestService;
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }
    @GetMapping("/get-all")
    public ApiResponse<List<RequestDTO>> getRequests(@RequestHeader String token) {
        return requestService.listBranchRequests(token);
    }
    @PostMapping("/update-status")
    public ApiResponse<RequestDTO> updateRequest(@RequestBody RequestDTO requestDTO, @RequestHeader String token) {
        return requestService.updateRequestFromBranch(requestDTO,token);
    }

}
