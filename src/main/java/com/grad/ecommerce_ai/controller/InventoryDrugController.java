package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.InventoryDrug;
import com.grad.ecommerce_ai.service.InventoryDrugService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/inventory-drugs")
public class InventoryDrugController {

    private final InventoryDrugService inventoryDrugService;

    public InventoryDrugController(InventoryDrugService inventoryDrugService) {
        this.inventoryDrugService = inventoryDrugService;
    }

    // Save InventoryDrug
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<InventoryDrug>> saveInventoryDrug(@RequestBody InventoryDrug inventoryDrug, @RequestHeader("Authorization") String token) {
        ApiResponse<InventoryDrug> response = inventoryDrugService.saveInventoryDrug(inventoryDrug, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Get InventoryDrug by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDrug>> getInventoryDrugById(@PathVariable String id, @RequestHeader("Authorization") String token) {
        ApiResponse<InventoryDrug> response = inventoryDrugService.getInventoryDrugById(id, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Get All Drugs for a Branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<InventoryDrug>>> getAllDrugsForBranch(@PathVariable Long branchId, @RequestHeader("Authorization") String token) {
        ApiResponse<List<InventoryDrug>> response = inventoryDrugService.getAllDrugsForBranch(branchId, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Update InventoryDrug
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<InventoryDrug>> updateInventoryDrug(@PathVariable String id, @RequestBody InventoryDrug inventoryDrug, @RequestHeader("Authorization") String token) {
        ApiResponse<InventoryDrug> response = inventoryDrugService.updateInventoryDrug(id, inventoryDrug, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Delete InventoryDrug
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInventoryDrug(@PathVariable String id, @RequestHeader("Authorization") String token) {
        ApiResponse<Void> response = inventoryDrugService.deleteInventoryDrug(id, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }
}
