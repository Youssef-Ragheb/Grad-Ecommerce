package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.DrugResponseDto;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.enitity.Drugs;
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
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>>getAllDrugsByName(@PathVariable String name){
        ApiResponse<List<DrugResponseDto>> apiResponse = drugService.getDrugsByDrugName(name);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<DrugResponseDto>>>getAllDrugsByCategory(@PathVariable String categoryId){
        ApiResponse<List<DrugResponseDto>> apiResponse = drugService.getDrugsWithCategory(categoryId);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
    @PostMapping("/add/main")
    public ResponseEntity<ApiResponse<Drugs>>getAllDrugsByCategory(@RequestBody Drugs drug,@RequestHeader String token){
        ApiResponse<Drugs> apiResponse = drugService.addDrugToMain(drug,token);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<InventoryDrugDTO>>updateDrug(@RequestBody InventoryDrugDTO drug,@PathVariable Long userId, @RequestParam String drugId ){
        ApiResponse<InventoryDrugDTO> apiResponse = drugService.updateDrug(drugId, drug,userId);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }
    @GetMapping("/{drugId}/branches")
    public ResponseEntity<ApiResponse<List<BranchDTO>>>updateDrug(@PathVariable String drugId){
        ApiResponse<List<BranchDTO>> apiResponse = drugService.getDrugFromInventory(drugId);
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }

}
