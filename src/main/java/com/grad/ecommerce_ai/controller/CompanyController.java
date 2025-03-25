package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.CompanyDTO;
import com.grad.ecommerce_ai.entity.Company;
import com.grad.ecommerce_ai.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("get/id")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompanyById(@RequestHeader String token) {
       ApiResponse<CompanyDTO> response = companyService.getCompanyWithToken(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("get/all")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getAllPharmacies() {
        ApiResponse<List<CompanyDTO>> response = companyService.getAllCompanies();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> getPharmacyById(@PathVariable Long id) {
        ApiResponse<CompanyDTO> response = companyService.getCompanyById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CompanyDTO>> createPharmacy(@RequestBody Company company,@RequestHeader String token) {
        ApiResponse<CompanyDTO> response = companyService.createCompany(company,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> updatePharmacy(
            @PathVariable Long id,
            @RequestBody Company company,@RequestHeader String token) {
        company.setCompanyId(id);
        ApiResponse<CompanyDTO> response = companyService.updateCompany( id,  company,  token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deletePharmacy(@PathVariable Long id,@RequestHeader String token) {
        ApiResponse<Boolean> response = companyService.deleteCompany(id,token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/get/{id}/branches")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getAllBranches(@PathVariable Long id) {
        ApiResponse<List<BranchDTO>> response = companyService.getCompanyBranches(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

