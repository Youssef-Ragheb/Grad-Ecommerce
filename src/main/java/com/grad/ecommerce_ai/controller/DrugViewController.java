package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.DrugResponseDetailsDto;
import com.grad.ecommerce_ai.dto.DrugResponseDto;
import com.grad.ecommerce_ai.service.DrugResponseToClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/drugs-view")
public class DrugViewController {

    private final DrugResponseToClientService drugResponseService;

    public DrugViewController(DrugResponseToClientService drugResponseService) {
        this.drugResponseService = drugResponseService;
    }

    // Get all branches that have a specific drug
    @GetMapping("/{drugId}/branches")
    public ApiResponse<List<BranchDTO>> getBranchesHaveDrug(@PathVariable String drugId) {
        return drugResponseService.getBranchesHaveDrug(drugId);
    }

    // Get all drugs in a specific category
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<DrugResponseDto>> getDrugsWithCategory(@PathVariable String categoryId) {
        return drugResponseService.getDrugsWithCategory(categoryId);
    }

    // Search for drugs by drug name
    @GetMapping("/search")
    public ApiResponse<List<DrugResponseDto>> searchDrugsByName(@RequestParam("name") String drugName) {
        return drugResponseService.getDrugsByDrugName(drugName);
    }

    // Get detailed view of a specific drug by its ID
    @GetMapping("/{drugId}/details")
    public ApiResponse<DrugResponseDetailsDto> getDrugDetails(@PathVariable String drugId) {
        return drugResponseService.getDrugDetailsView(drugId);
    }
}

