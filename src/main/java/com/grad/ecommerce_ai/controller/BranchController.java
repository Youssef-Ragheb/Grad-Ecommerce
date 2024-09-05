package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getAllBranches() {
        ApiResponse<List<BranchDTO>> response = branchService.getAllBranches();
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDTO>> getBranchById(@PathVariable Long id) {
        ApiResponse<BranchDTO> response = branchService.getBranchById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BranchDTO>> createBranch(@RequestBody BranchDTO branch, @RequestParam Long companyId, @RequestHeader String token) {
        ApiResponse<BranchDTO> response = branchService.createBranch(branch,companyId, token);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("/branches/pharmacy/{pharmacyId}")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getBranchesByPharmacyId(@PathVariable Long pharmacyId) {
        ApiResponse<List<BranchDTO>> response = branchService.getBranchesByPharmacyId(pharmacyId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDTO>> updateBranch(@PathVariable Long id, @RequestBody @Valid BranchDTO branch, @RequestHeader String token) {
        ApiResponse<BranchDTO> response = branchService.updateBranch(id, branch,token);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BranchDTO>> deleteBranch(@PathVariable Long id,@RequestHeader String token) {
        ApiResponse<BranchDTO> response = branchService.deleteBranch(id,token);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
}

