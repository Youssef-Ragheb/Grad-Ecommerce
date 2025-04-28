package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Drugs;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.InventoryDrugRepository;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.*;
import static com.grad.ecommerce_ai.mappers.InventoryDrugMapper.inventoryDrugDtoToInventoryDrug;
import static com.grad.ecommerce_ai.mappers.InventoryDrugMapper.inventoryDrugToDto;

@Service
public class InventoryDrugService {
    private final InventoryDrugRepository inventoryDrugRepository;
    private final JwtService jwtService;
    private final CheckAuth checkAuth;
    private final MainDrugRepository mainDrugRepository;
    private final BranchRepository branchRepository;

    public InventoryDrugService(InventoryDrugRepository inventoryDrugRepository, JwtService jwtService, CheckAuth checkAuth, MainDrugRepository mainDrugRepository, BranchRepository branchRepository) {
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.jwtService = jwtService;
        this.checkAuth = checkAuth;
        this.mainDrugRepository = mainDrugRepository;
        this.branchRepository = branchRepository;
    }

    public ApiResponse<InventoryDrugDTO> saveInventoryDrug(InventoryDrugDTO inventoryDrugDTO, String token,Long branchId) {
        ApiResponse<InventoryDrugDTO> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        if (!checkAuth.checkAuthToBranch(userId, branchId)) {
            response.setStatus(false);
            response.setMessage("unauthorized");
            response.setStatusCode(401);
            return response;
        }
        Optional<Branch> branch = branchRepository.findById(branchId);
        if(branch.isEmpty()){
            response.setStatus(false);
            response.setMessage("branch not found");
            response.setStatusCode(404);
            return response;
        }
        inventoryDrugDTO.setBranchDTO(branchToDto(branch.get()));
        Optional<Drugs> drug = mainDrugRepository.findById(inventoryDrugDTO.getDrugDTO().getDrugId());
        if (drug.isEmpty()) {
            response.setStatus(false);
            response.setMessage("drug not found");
            response.setStatusCode(404);
            return response;
        }
        InventoryDrug inventoryDrug = inventoryDrugRepository.save(inventoryDrugDtoToInventoryDrug(inventoryDrugDTO));
        response.setStatus(true);
        response.setMessage("success");
        response.setStatusCode(200);
        response.setData(inventoryDrugToDto(inventoryDrug));
        return response;
    }

    public ApiResponse<InventoryDrugDTO> getInventoryDrugById(Long id, String token) {
        ApiResponse<InventoryDrugDTO> response = new ApiResponse<>();
        try {
            InventoryDrug inventoryDrug = inventoryDrugRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, inventoryDrug.getBranch().getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }

            response.setData(inventoryDrugToDto(inventoryDrug));
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

    public ApiResponse<List<InventoryDrugDTO>> getAllDrugsForBranch(Long branchId, String token) {
        ApiResponse<List<InventoryDrugDTO>> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        if (!checkAuth.checkAuthToBranch(userId, branchId)) {
            response.setStatus(false);
            response.setMessage("unauthorized");
            response.setStatusCode(401);
            return response;
        }

        try {

            List<InventoryDrug> inventoryDrugs = inventoryDrugRepository.findAllByBranch_BranchId(branchId);
            List<InventoryDrugDTO> inventoryDrugDTOS = new ArrayList<>();

            for(InventoryDrug inventoryDrug : inventoryDrugs){
                inventoryDrugDTOS.add(inventoryDrugToDto(inventoryDrug));
            }
            response.setData(inventoryDrugDTOS);
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

    public ApiResponse<Void> deleteInventoryDrug(Long id, String token) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            InventoryDrug inventoryDrug = inventoryDrugRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, inventoryDrug.getBranch().getBranchId())) {
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

    public ApiResponse<InventoryDrugDTO> updateInventoryDrug(Long id, InventoryDrugDTO inventoryDrugDTO, String token) {
        ApiResponse<InventoryDrugDTO> response = new ApiResponse<>();
        try {
            // Fetch the existing InventoryDrug by id
            InventoryDrug existingInventoryDrug = inventoryDrugRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            // Check user authorization for branch access
            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, existingInventoryDrug.getBranch().getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }
            Optional<Drugs> drug = mainDrugRepository.findById(existingInventoryDrug.getDrug().getId());
            if (drug.isEmpty()) {
                response.setStatus(false);
                response.setStatusCode(404);
                response.setMessage("drug not found ");
                return response;
            }

            // Update the fields of the existing inventory drug
            existingInventoryDrug.setDrug(drugDtoToDrug(inventoryDrugDTO.getDrugDTO()));
            existingInventoryDrug.setPrice(inventoryDrugDTO.getPrice());
            existingInventoryDrug.setStock(inventoryDrugDTO.getStock());
            existingInventoryDrug.setBranch(branchDTOToBranch(inventoryDrugDTO.getBranchDTO()));

            // Save the updated inventory drug
            InventoryDrug updatedInventoryDrug = inventoryDrugRepository.save(existingInventoryDrug);

            // Prepare the response
            response.setData(inventoryDrugToDto(updatedInventoryDrug));
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
