package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.service.DrugService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drug")
public class DrugController {
    private final DrugService drugService;

    public DrugController(DrugService drugService) {
        this.drugService = drugService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<InventoryDrugDTO>> addDrug(@RequestBody InventoryDrugDTO drug, @RequestHeader String token){
        ApiResponse<InventoryDrugDTO> apiResponse = drugService.addDrug(drug,token);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<InventoryDrugDTO>>> getBranchDrugs(@PathVariable Long branchId){
        ApiResponse<List<InventoryDrugDTO>> apiResponse = drugService.getBranchDrags(branchId);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
}
