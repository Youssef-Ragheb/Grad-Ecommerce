package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.DrugResponseDto;
import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.enitity.InventoryDrug;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.repository.*;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.InventoryDrugMapper.*;

@Service
public class DrugService {
    private final InventoryDrugRepository inventoryDrugRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final ActiveIngredientService activeIngredientService;

    private final CheckAuth checkAuth;
    private final MainDrugRepository mainDrugRepository;
    private final CategoryRepository categoryRepository;

    public DrugService(InventoryDrugRepository inventoryDrugRepository, BranchRepository branchRepository, UserRepository userRepository, JwtService jwtService, ActiveIngredientService activeIngredientService, CheckAuth checkAuth, MainDrugRepository mainDrugRepository, CategoryRepository categoryRepository) {
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.activeIngredientService = activeIngredientService;
        this.checkAuth = checkAuth;
        this.mainDrugRepository = mainDrugRepository;
        this.categoryRepository = categoryRepository;
    }

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
    public ApiResponse<Drugs>addDrugToMain(Drugs drug,String token) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        if(!jwtService.isAdmin(token)){
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
    public ApiResponse<List<DrugResponseDto>> getDrugsWithCategory(String categoryId) {

        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();

        List<DrugResponseDto> drugResponseList = new ArrayList<>();
        float averagePrice = 0;

        boolean isAvailable = false;
        List<Drugs> drugsList = mainDrugRepository.findByCategoryId(categoryId);

        int totalDrugs = drugsList.size();

        for(int i = 0; i < totalDrugs; i++) {

            DrugResponseDto drug = new DrugResponseDto();
            drug.setDrugName(drugsList.get(i).getDrugName());
            drug.setImageUrl(drugsList.get(i).getLogo());
            drug.setDescription(drugsList.get(i).getDescription());
            drug.setDrugId(drugsList.get(i).getId());

            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugsList.get(i).getId());
            int inventoryDrugListSize = inventoryDrugList.size();
            float averageTotalPrice = 0;

            for (int j = 0; j < inventoryDrugListSize; j++) {
                if(inventoryDrugList.get(i).getStock() > 0){
                    isAvailable = true;
                }
                averageTotalPrice +=  inventoryDrugList.get(i).getPrice();
            }
            averagePrice = averageTotalPrice/inventoryDrugListSize;
            drug.setAvailable(isAvailable);
            drug.setPrice(averagePrice);
            drugResponseList.add(drug);
        }
        response.setData(drugResponseList);
        response.setMessage("Drugs found");
        response.setStatusCode(200);
        response.setStatus(true);

        return response;
    }
    public ApiResponse<List<DrugResponseDto>> getDrugsByDrugName(String drugName){
        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();

        List<DrugResponseDto> drugResponseList = new ArrayList<>();
        float averagePrice = 0;

        boolean isAvailable = false;
        List<Drugs> drugsList = mainDrugRepository.DrugNameContainingIgnoreCase(drugName);

        int totalDrugs = drugsList.size();
        for(int i = 0; i < totalDrugs; i++) {

            DrugResponseDto drug = new DrugResponseDto();
            drug.setDrugName(drugsList.get(i).getDrugName());
            drug.setImageUrl(drugsList.get(i).getLogo());
            drug.setDescription(drugsList.get(i).getDescription());
            drug.setDrugId(drugsList.get(i).getId());

            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugsList.get(i).getId());
            int inventoryDrugListSize = inventoryDrugList.size();
            float averageTotalPrice = 0;

            for (int j = 0; j < inventoryDrugListSize; j++) {
                if(inventoryDrugList.get(i).getStock() > 0){
                    isAvailable = true;
                }
                averageTotalPrice +=  inventoryDrugList.get(i).getPrice();
            }
            averagePrice = averageTotalPrice/inventoryDrugListSize;
            drug.setAvailable(isAvailable);
            drug.setPrice(averagePrice);
            drugResponseList.add(drug);
        }
        response.setData(drugResponseList);
        response.setMessage("Drugs found");
        response.setStatusCode(200);
        response.setStatus(true);

        return response;
    }

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
