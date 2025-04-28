package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.entity.InventoryDrug;
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
    public ResponseEntity<ApiResponse<InventoryDrugDTO>> saveInventoryDrug(@RequestBody InventoryDrugDTO inventoryDrug, @RequestHeader String token,Long branchId) {
        ApiResponse<InventoryDrugDTO> response = inventoryDrugService.saveInventoryDrug(inventoryDrug, token,branchId);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Get InventoryDrug by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDrugDTO>> getInventoryDrugById(@PathVariable Long id, @RequestHeader String token) {
        ApiResponse<InventoryDrugDTO> response = inventoryDrugService.getInventoryDrugById(id, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Get All Drugs for a Branch
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<InventoryDrugDTO>>> getAllDrugsForBranch(@PathVariable Long branchId, @RequestHeader String token) {
        ApiResponse<List<InventoryDrugDTO>> response = inventoryDrugService.getAllDrugsForBranch(branchId, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Update InventoryDrug
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<InventoryDrugDTO>> updateInventoryDrug(@PathVariable Long id, @RequestBody InventoryDrugDTO inventoryDrugDTO, @RequestHeader String token) {
        ApiResponse<InventoryDrugDTO> response = inventoryDrugService.updateInventoryDrug(id, inventoryDrugDTO, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    // Delete InventoryDrug
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInventoryDrug(@PathVariable Long id, @RequestHeader String token) {
        ApiResponse<Void> response = inventoryDrugService.deleteInventoryDrug(id, token);
        return new ResponseEntity<>(response, response.getStatusCode() == 200 ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.UNAUTHORIZED);
    }
}
