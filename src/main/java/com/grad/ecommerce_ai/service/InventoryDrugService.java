package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.InventoryDrug;
import com.grad.ecommerce_ai.repository.InventoryDrugRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryDrugService {
    private final InventoryDrugRepository inventoryDrugRepository;
    private final JwtService jwtService;
    private final CheckAuth checkAuth;

    public InventoryDrugService(InventoryDrugRepository inventoryDrugRepository, JwtService jwtService, CheckAuth checkAuth) {
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.jwtService = jwtService;
        this.checkAuth = checkAuth;
    }
    public ApiResponse<InventoryDrug> saveInventoryDrug(InventoryDrug inventoryDrug, String token){
        ApiResponse<InventoryDrug> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        if(!checkAuth.checkAuthToBranch(userId,inventoryDrug.getBranchId())){
            response.setStatus(false);
            response.setMessage("unauthorized");
            response.setStatusCode(401);
            return response;
        }
        response.setStatus(true);
        response.setMessage("success");
        response.setData(inventoryDrugRepository.save(inventoryDrug));
        return response;
    }

    public ApiResponse<InventoryDrug> getInventoryDrugById(String id, String token) {
        ApiResponse<InventoryDrug> response = new ApiResponse<>();
        try {
            InventoryDrug inventoryDrug = inventoryDrugRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, inventoryDrug.getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }

            response.setData(inventoryDrug);
            response.setMessage("success");
            response.setStatus(true);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error retrieving inventory drug: " + e.getMessage());
            response.setStatusCode(400);
            response.setData(null);
        }
        return response;
    }

    public ApiResponse<List<InventoryDrug>> getAllDrugsForBranch(Long branchId, String token) {
        ApiResponse<List<InventoryDrug>> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        if (!checkAuth.checkAuthToBranch(userId, branchId)) {
            response.setStatus(false);
            response.setMessage("unauthorized");
            response.setStatusCode(401);
            return response;
        }

        try {
            List<InventoryDrug> inventoryDrugs = inventoryDrugRepository.findAllByBranchId(branchId);
            response.setData(inventoryDrugs);
            response.setMessage("success");
            response.setStatus(true);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error retrieving inventory drugs: " + e.getMessage());
            response.setStatusCode(400);
            response.setData(null);
        }

        return response;
    }

    public ApiResponse<Void> deleteInventoryDrug(String id, String token) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            InventoryDrug inventoryDrug = inventoryDrugRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, inventoryDrug.getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }

            inventoryDrugRepository.delete(inventoryDrug);
            response.setMessage("Inventory Drug deleted successfully");
            response.setStatus(true);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error deleting inventory drug: " + e.getMessage());
            response.setStatusCode(400);
        }
        return response;
    }

    public ApiResponse<InventoryDrug> updateInventoryDrug(String id, InventoryDrug inventoryDrug, String token) {
        ApiResponse<InventoryDrug> response = new ApiResponse<>();
        try {
            // Fetch the existing InventoryDrug by id
            InventoryDrug existingInventoryDrug = inventoryDrugRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            // Check user authorization for branch access
            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, existingInventoryDrug.getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }

            // Update the fields of the existing inventory drug
            existingInventoryDrug.setDrugId(inventoryDrug.getDrugId());
            existingInventoryDrug.setCategoryId(inventoryDrug.getCategoryId());
            existingInventoryDrug.setActiveIngredientId(inventoryDrug.getActiveIngredientId());
            existingInventoryDrug.setPrice(inventoryDrug.getPrice());
            existingInventoryDrug.setStock(inventoryDrug.getStock());
            existingInventoryDrug.setBranchId(inventoryDrug.getBranchId());

            // Save the updated inventory drug
            InventoryDrug updatedInventoryDrug = inventoryDrugRepository.save(existingInventoryDrug);

            // Prepare the response
            response.setData(updatedInventoryDrug);
            response.setMessage("Inventory Drug updated successfully");
            response.setStatus(true);
            response.setStatusCode(200);
        } catch (Exception e) {
            // Handle errors and send an appropriate response
            response.setStatus(false);
            response.setMessage("Error updating inventory drug: " + e.getMessage());
            response.setStatusCode(400);
            response.setData(null);
        }
        return response;
    }

}
