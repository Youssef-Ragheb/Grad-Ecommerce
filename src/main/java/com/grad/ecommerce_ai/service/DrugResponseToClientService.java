package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.DrugResponseDetailsDto;
import com.grad.ecommerce_ai.dto.DrugResponseDto;
import com.grad.ecommerce_ai.entity.ActiveIngredient;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Drugs;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import com.grad.ecommerce_ai.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.grad.ecommerce_ai.mappers.DtoConverter.branchToDto;


@Service
public class DrugResponseToClientService {
    //TODO create template for products view
    //TODO create template for one product view with more details
    private final MainDrugRepository mainDrugRepository;
    private final InventoryDrugRepository inventoryDrugRepository;
    private final ActiveIngredientRepository activeIngredientRepository;
    private final BranchRepository branchRepository;

    public DrugResponseToClientService(MainDrugRepository mainDrugRepository, InventoryDrugRepository inventoryDrugRepository, ActiveIngredientRepository activeIngredientRepository, BranchRepository branchRepository) {
        this.mainDrugRepository = mainDrugRepository;
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.activeIngredientRepository = activeIngredientRepository;
        this.branchRepository = branchRepository;
    }

    //TODO view products..
    public ApiResponse<List<BranchDTO>> getBranchesHaveDrug(String drugId) {
        ApiResponse<List<BranchDTO>> response = new ApiResponse<>();

        // Check if the drug exists
        if (!mainDrugRepository.existsById(drugId)) {
            response.setData(null);
            response.setMessage("Drug not found");
            response.setStatusCode(404);
            response.setStatus(false);
            return response;
        }

        // Get all inventory items with the specified drugId
        List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugId);
        List<Long> branchIds = inventoryDrugList.stream()
                .filter(inventoryDrug -> inventoryDrug.getStock() > 0) // Only branches with stock > 0
                .map(InventoryDrug::getBranchId)
                .distinct()
                .collect(Collectors.toList());

        // Batch fetch branches by IDs
        List<Branch> branches = branchRepository.findAllById(branchIds);

        // Map branches by their ID for quick lookup
        Map<Long, Branch> branchMap = branches.stream()
                .collect(Collectors.toMap(Branch::getBranchId, branch -> branch));

        List<BranchDTO> branchDTOS = new ArrayList<>();
        for (InventoryDrug inventoryDrug : inventoryDrugList) {
            if (inventoryDrug.getStock() > 0) {
                Branch branch = branchMap.get(inventoryDrug.getBranchId());
                if (branch != null) {
                    BranchDTO branchDTO = branchToDto(branch);
                    branchDTO.setPrice(inventoryDrug.getPrice());
                    branchDTOS.add(branchDTO);
                }
            }
        }
        if(branchDTOS.isEmpty()){
            response.setData(null);
            response.setStatusCode(404);
            response.setStatus(false);
            response.setMessage("drug not found in any branch");
            return response;
        }
        // Prepare the response
        response.setData(branchDTOS);
        response.setMessage("Branches that have the drug");
        response.setStatusCode(200);
        response.setStatus(true);

        return response;
    }
    public ApiResponse<List<DrugResponseDto>> getDrugsWithCategory(String categoryId) {

        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();
        List<DrugResponseDto> drugResponseList = new ArrayList<>();

        List<Drugs> drugsList = mainDrugRepository.findByCategoryId(categoryId);

        return getListApiResponse(response, drugResponseList, drugsList);
    }

    public ApiResponse<List<DrugResponseDto>> getDrugsByDrugName(String drugName) {
        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();

        List<DrugResponseDto> drugResponseList = new ArrayList<>();



        List<Drugs> drugsList = mainDrugRepository.DrugNameContainingIgnoreCase(drugName);

        return getListApiResponse(response, drugResponseList, drugsList);
    }

    private ApiResponse<List<DrugResponseDto>> getListApiResponse(
            ApiResponse<List<DrugResponseDto>> response,
            List<DrugResponseDto> drugResponseList, List<Drugs> drugsList) {
        List<String> drugIds = drugsList.stream().map(Drugs::getId).collect(Collectors.toList());

        List<InventoryDrug> allInventoryDrugs = inventoryDrugRepository.findAllByDrugIdIn(drugIds);
        Map<String, List<InventoryDrug>> inventoryByDrugId = allInventoryDrugs.stream()
                .collect(Collectors.groupingBy(InventoryDrug::getDrugId));
        for (Drugs drugEntity : drugsList) {
            DrugResponseDto drugResponse = new DrugResponseDto();
            drugResponse.setDrugName(drugEntity.getDrugName());
            drugResponse.setImageUrl(drugEntity.getLogo());
            drugResponse.setDescription(drugEntity.getDescription());
            drugResponse.setDrugId(drugEntity.getId());

            List<InventoryDrug> inventoryDrugs = inventoryByDrugId.getOrDefault(drugEntity.getId(), new ArrayList<>());

            // Calculate average price and availability
            if (!inventoryDrugs.isEmpty()) {
                double averagePrice = inventoryDrugs.stream().mapToDouble(InventoryDrug::getPrice).average().orElse(0);
                boolean isAvailable = inventoryDrugs.stream().anyMatch(inventory -> inventory.getStock() > 0);
                drugResponse.setPrice((float) averagePrice);
                drugResponse.setAvailable(isAvailable);
            } else {
                // Default values if no inventory found
                drugResponse.setPrice(0);
                drugResponse.setAvailable(false);
            }

            drugResponseList.add(drugResponse);
        }

        response.setData(drugResponseList);
        response.setMessage("Drugs found");
        response.setStatusCode(200);
        response.setStatus(true);

        return response;
    }

    //TODO view product details..
    public ApiResponse<DrugResponseDetailsDto> getDrugDetailsView(String drugId) {
        ApiResponse<DrugResponseDetailsDto> response = new ApiResponse<>();
        try {
            Drugs drug = mainDrugRepository.findById(drugId).orElseThrow();
            DrugResponseDetailsDto drugResponseDetailsDto = new DrugResponseDetailsDto();
            drugResponseDetailsDto.setDrugId(drug.getId());
            drugResponseDetailsDto.setDrugName(drug.getDrugName());
            drugResponseDetailsDto.setImageUrl(drug.getLogo());
            drugResponseDetailsDto.setDescription(drug.getDescription());
            float averagePrice;
            //
            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugId);
            int inventoryDrugListSize = inventoryDrugList.size();
            float averageTotalPrice = 0;
            boolean isAvailable = false;
            for (InventoryDrug inventoryDrug : inventoryDrugList) {
                if (inventoryDrug.getStock() > 0) {
                    isAvailable = true;
                }
                averageTotalPrice += inventoryDrug.getPrice();
            }
            averagePrice = averageTotalPrice / inventoryDrugListSize;
            drugResponseDetailsDto.setAvailable(isAvailable);
            drugResponseDetailsDto.setPrice(averagePrice);
            //active and category name
            Optional<ActiveIngredient> activeIngredient = activeIngredientRepository.findById(drug.getActiveIngredientId());
            if (activeIngredient.isEmpty()) {
                response.setMessage("active ingredient is not found");
                response.setStatusCode(404);
                response.setStatus(false);
                return response;
            }
            drugResponseDetailsDto.setActiveIngredients(activeIngredient.get().getActiveIngredient());
//            Optional<Category> category = categoryRepository.findById(drug.getCategoryId());
//            if (category.isEmpty()) {
//                response.setMessage("category is not found");
//                response.setStatusCode(404);
//                response.setStatus(false);
//                return response;
//            }
//            String CategoryName = category.get().getCategoryName();
//            drugResponseDetailsDto.setCategoryName(CategoryName);
            response.setStatusCode(200);
            response.setStatus(true);
            response.setData(drugResponseDetailsDto);
            response.setMessage("found");
            return response;
        } catch (Exception e) {
            response.setData(null);
            response.setStatusCode(404);
            response.setStatus(false);
            response.setMessage("drug not found");
            return response;
        }



    }

}