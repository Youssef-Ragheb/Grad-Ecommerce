package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.entity.Status;
import com.grad.ecommerce_ai.repository.RequestRepository;
import com.grad.ecommerce_ai.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/request")
public class RequestController {
    private final RequestService requestService;
    private final RequestRepository requestRepository;

    public RequestController(RequestService requestService, RequestRepository requestRepository) {
        this.requestService = requestService;
        this.requestRepository = requestRepository;
    }
    @GetMapping("/get-all")
    public ApiResponse<List<RequestDTO>> getRequests(@RequestHeader String token) {
        return requestService.listBranchRequests(token);
    }
    //listBranchRequestsByCompany
    @GetMapping("/get-all/by-company/{branchId}")
    public ApiResponse<List<RequestDTO>> listBranchRequestsByCompany(@PathVariable Long branchId) {
        return requestService.listBranchRequestsByCompany(branchId);
    }
    @PostMapping("/update-status")
    public ApiResponse<RequestDTO> updateRequest(@RequestBody RequestDTO requestDTO, @RequestHeader String token) {
        return requestService.updateRequestFromBranch(requestDTO,token);
    }
    //updateRequestFromCompany
    @PostMapping("/update-status/by-company")
    public ApiResponse<RequestDTO> updateRequestByCompany(@RequestBody RequestDTO requestDTO, @RequestHeader String token) {
        return requestService.updateRequestFromCompany(requestDTO,token);
    }

    @PostMapping("/pending")
    public Long getPendingRequests(@RequestBody List<Long> branchIds) {
        return requestRepository.countByBranchIdInAndStatus(branchIds,Status.PENDING);
    }

}
