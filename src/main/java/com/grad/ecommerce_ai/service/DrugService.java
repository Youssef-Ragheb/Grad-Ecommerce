package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.enitity.InventoryDrug;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.InventoryDrugRepository;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.InventoryDrugMapper.*;

@Service
public class DrugService {
    private final InventoryDrugRepository inventoryDrugRepository;
    private final BranchRepository branchRepository;
    private final JwtService jwtService;
    private final ActiveIngredientService activeIngredientService;
    private final CheckAuth checkAuth;
    private final MainDrugRepository mainDrugRepository;


    public DrugService(InventoryDrugRepository inventoryDrugRepository, BranchRepository branchRepository, JwtService jwtService, ActiveIngredientService activeIngredientService, CheckAuth checkAuth, MainDrugRepository mainDrugRepository) {
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.branchRepository = branchRepository;
        this.jwtService = jwtService;
        this.activeIngredientService = activeIngredientService;
        this.checkAuth = checkAuth;
        this.mainDrugRepository = mainDrugRepository;

    }
/*
    public ApiResponse<InventoryDrugDTO> addDrug(InventoryDrugDTO dragDto, String token) {
        // TODO get the id from token when we add security
        ApiResponse<InventoryDrugDTO> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<Branch> branch = branchRepository.findById(dragDto.getBranchId());
        if (branch.isEmpty()) {
            response.setMessage("Branch not found");
            response.setStatusCode(404);
            response.setStatus(false);
            return response;
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            response.setMessage("User not found");
            response.setStatusCode(404);
            response.setStatus(false);
            return response;
        }

        if (!(checkAuth.checkAuthToBranch(userId, dragDto.getBranchId()))) {
            response.setMessage("unauthorized");
            response.setStatusCode(401);
            response.setStatus(false);
            return response;
        }
        ApiResponse<Boolean> checkActiveIngredient = activeIngredientService.checkDrugActiveIngredient(dragDto.getActiveIngredientId(), dragDto.getDrugId());
        if (!checkActiveIngredient.getData()) {
            response.setMessage(checkActiveIngredient.getMessage());
            response.setStatusCode(checkActiveIngredient.getStatusCode());
            response.setStatus(false);
            return response;
        }

        InventoryDrug inventoryDrug = dtoToDrug(dragDto);

        response.setData(drugToDto(inventoryDrugRepository.save(inventoryDrug)));
        response.setStatus(true);
        response.setMessage("Drug Added");
        return response;
    }

    public ApiResponse<List<InventoryDrugDTO>> getBranchDrags(Long branchId) {
        ApiResponse<List<InventoryDrugDTO>> apiResponse = new ApiResponse<>();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            apiResponse.setMessage("Branch not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        apiResponse.setData(drugListToDtoList(inventoryDrugRepository.findAllByBranchId(branchId)));
        apiResponse.setMessage("Drugs found");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        return apiResponse;
    }
  */
    public ApiResponse<Drugs> addDrugToMain(Drugs drug, String token) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        if (jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setData(null);
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        apiResponse.setData(mainDrugRepository.save(drug));
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("drug added");
        return apiResponse;
    }

    // Read a single drug by ID
    public ApiResponse<Drugs> getDrugById(String id) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        Optional<Drugs> drugOpt = mainDrugRepository.findById(id);
        if (drugOpt.isEmpty()) {
            apiResponse.setMessage("Drug not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        apiResponse.setData(drugOpt.get());
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Drug retrieved successfully");
        return apiResponse;
    }

    // Get a list of all drugs
    public ApiResponse<List<Drugs>> getAllDrugs() {
        ApiResponse<List<Drugs>> apiResponse = new ApiResponse<>();
        List<Drugs> drugsList = mainDrugRepository.findAll();
        apiResponse.setData(drugsList);
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("All drugs retrieved successfully");
        return apiResponse;
    }

    // Update an existing drug by ID
    public ApiResponse<Drugs> updateDrug(String id, Drugs updatedDrug, String token) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        if (jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setData(null);
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<Drugs> existingDrugOpt = mainDrugRepository.findById(id);
        if (existingDrugOpt.isEmpty()) {
            apiResponse.setMessage("Drug not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Drugs existingDrug = existingDrugOpt.get();
        existingDrug.setActiveIngredientId(updatedDrug.getActiveIngredientId());
        existingDrug.setCategoryId(updatedDrug.getCategoryId());
        existingDrug.setDrugName(updatedDrug.getDrugName());
        existingDrug.setDescription(updatedDrug.getDescription());
        existingDrug.setLogo(updatedDrug.getLogo());

        Drugs savedDrug = mainDrugRepository.save(existingDrug);
        apiResponse.setData(savedDrug);
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Drug updated successfully");
        return apiResponse;
    }

    // Delete an existing drug by ID
    public ApiResponse<Void> deleteDrug(String id, String token) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        if (jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<Drugs> existingDrugOpt = mainDrugRepository.findById(id);
        if (existingDrugOpt.isEmpty()) {
            apiResponse.setMessage("Drug not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        mainDrugRepository.deleteById(id);
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Drug deleted successfully");
        return apiResponse;
    }
    // Method to search drugs by name
    public ApiResponse<List<Drugs>> searchDrugsByName(String name) {
        ApiResponse<List<Drugs>> response = new ApiResponse<>();
        try {
            List<Drugs> drugs = mainDrugRepository.findByDrugNameContainingIgnoreCase(name);
            response.setData(drugs);
            response.setMessage("Drugs found with name containing: " + name);
            response.setStatus(true);
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error: " + e.getMessage());
            response.setStatusCode(400);
        }
        return response;
    }

    // Get branches that have the drug

    public ApiResponse<InventoryDrugDTO> updateDrug(String drugId, InventoryDrugDTO inventoryDrugDto, Long userId) {
        ApiResponse<InventoryDrugDTO> response = new ApiResponse<>();

        Optional<InventoryDrug> existingDrugOpt = inventoryDrugRepository.findById(drugId);
        if (existingDrugOpt.isEmpty()) {
            response.setMessage("Drug not found");
            response.setStatusCode(404);
            response.setStatus(false);
            return response;
        }

        InventoryDrug existingInventoryDrug = existingDrugOpt.get();

        if (!checkAuth.checkAuthToBranch(userId, existingInventoryDrug.getBranchId())) {
            response.setMessage("Unauthorized");
            response.setStatusCode(401);
            response.setStatus(false);
            return response;
        }

        if (!existingInventoryDrug.getBranchId().equals(inventoryDrugDto.getBranchId())) {
            Optional<Branch> branchOpt = branchRepository.findById(inventoryDrugDto.getBranchId());
            if (branchOpt.isEmpty()) {
                response.setMessage("Branch not found");
                response.setStatusCode(404);
                response.setStatus(false);
                return response;
            }
        }

        ApiResponse<Boolean> checkActiveIngredient = activeIngredientService.checkDrugActiveIngredient(inventoryDrugDto.getActiveIngredientId(), inventoryDrugDto.getDrugId());
        if (!checkActiveIngredient.getData()) {
            response.setMessage(checkActiveIngredient.getMessage());
            response.setStatusCode(checkActiveIngredient.getStatusCode());
            response.setStatus(false);
            return response;
        }

        existingInventoryDrug.setDrugId(inventoryDrugDto.getDrugId());
        existingInventoryDrug.setCategoryId(inventoryDrugDto.getCategoryId());
        existingInventoryDrug.setActiveIngredientId(inventoryDrugDto.getActiveIngredientId());
        existingInventoryDrug.setPrice(inventoryDrugDto.getPrice());
        existingInventoryDrug.setStock(inventoryDrugDto.getStock());
        existingInventoryDrug.setBranchId(inventoryDrugDto.getBranchId());

        response.setData(drugToDto(inventoryDrugRepository.save(existingInventoryDrug)));
        response.setStatus(true);
        response.setMessage("Drug updated successfully");
        return response;
    }
}
