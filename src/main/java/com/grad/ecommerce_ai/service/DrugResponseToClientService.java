package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.DrugResponseDetailsDto;
import com.grad.ecommerce_ai.dto.DrugResponseDto;
import com.grad.ecommerce_ai.enitity.ActiveIngredient;
import com.grad.ecommerce_ai.enitity.Category;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.enitity.InventoryDrug;
import com.grad.ecommerce_ai.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.branchToDto;

@Service
public class DrugResponseToClientService {
    //TODO create template for products view
    //TODO create template for one product view with more details
    private final MainDrugRepository mainDrugRepository;
    private final InventoryDrugRepository inventoryDrugRepository;
    private final CategoryRepository categoryRepository;
    private final ActiveIngredientRepository activeIngredientRepository;
    private final BranchRepository branchRepository;

    public DrugResponseToClientService(MainDrugRepository mainDrugRepository, InventoryDrugRepository inventoryDrugRepository, CategoryRepository categoryRepository, ActiveIngredientRepository activeIngredientRepository, BranchRepository branchRepository) {
        this.mainDrugRepository = mainDrugRepository;
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.categoryRepository = categoryRepository;
        this.activeIngredientRepository = activeIngredientRepository;
        this.branchRepository = branchRepository;
    }

    //TODO view products..
    public ApiResponse<List<BranchDTO>> getBranchesHaveDrug(String drugId) {
        ApiResponse<List<BranchDTO>> response = new ApiResponse<>();
        if (!mainDrugRepository.existsById(drugId)) {
            response.setData(null);
            response.setMessage("Drug not found");
            response.setStatusCode(404);
            response.setStatus(false);
        }
        List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugId);
        List<BranchDTO> branchDTOS = new ArrayList<>();
        for (InventoryDrug inventoryDrug : inventoryDrugList) {
            if (inventoryDrug.getStock() > 0) {
                BranchDTO branchDTO =branchToDto(branchRepository.findById(inventoryDrug.getBranchId()).get());
                branchDTO.setPrice(inventoryDrug.getPrice());
                branchDTOS.add(branchDTO);
            }
        }
        response.setData(branchDTOS);
        response.setMessage("Branches that have the drug");
        response.setStatusCode(200);
        response.setStatus(true);

        return response;
    }

    public ApiResponse<List<DrugResponseDto>> getDrugsWithCategory(String categoryId) {

        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();

        List<DrugResponseDto> drugResponseList = new ArrayList<>();


        boolean isAvailable = false;
        List<Drugs> drugsList = mainDrugRepository.findByCategoryId(categoryId);

        return getListApiResponse(response, drugResponseList, isAvailable, drugsList);
    }

    public ApiResponse<List<DrugResponseDto>> getDrugsByDrugName(String drugName) {
        ApiResponse<List<DrugResponseDto>> response = new ApiResponse<>();

        List<DrugResponseDto> drugResponseList = new ArrayList<>();


        boolean isAvailable = false;
        List<Drugs> drugsList = mainDrugRepository.DrugNameContainingIgnoreCase(drugName);

        return getListApiResponse(response, drugResponseList, isAvailable, drugsList);
    }

    private ApiResponse<List<DrugResponseDto>> getListApiResponse(ApiResponse<List<DrugResponseDto>> response, List<DrugResponseDto> drugResponseList, boolean isAvailable, List<Drugs> drugsList) {
        float averagePrice;
        int totalDrugs = drugsList.size();
        for (int i = 0; i < totalDrugs; i++) {

            DrugResponseDto drug = new DrugResponseDto();
            drug.setDrugName(drugsList.get(i).getDrugName());
            drug.setImageUrl(drugsList.get(i).getLogo());
            drug.setDescription(drugsList.get(i).getDescription());
            drug.setDrugId(drugsList.get(i).getId());

            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByDrugId(drugsList.get(i).getId());
            int inventoryDrugListSize = inventoryDrugList.size();
            float averageTotalPrice = 0;

            for (int j = 0; j < inventoryDrugListSize; j++) {//loop 3li al adwya bta3t branches b3d ma at3ml filter
                if (inventoryDrugList.get(j).getStock() > 0) {
                    isAvailable = true;
                }else{
                    isAvailable = false;
                }
                averageTotalPrice += inventoryDrugList.get(j).getPrice();
            }
            averagePrice = averageTotalPrice / inventoryDrugListSize;
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
            for (int j = 0; j < inventoryDrugListSize; j++) {
                if (inventoryDrugList.get(j).getStock() > 0) {
                    isAvailable = true;
                }
                averageTotalPrice += inventoryDrugList.get(j).getPrice();
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
            Optional<Category> category = categoryRepository.findById(drug.getCategoryId());
            if (category.isEmpty()) {
                response.setMessage("category is not found");
                response.setStatusCode(404);
                response.setStatus(false);
                return response;
            }
            String CategoryName = category.get().getCategoryName();
            drugResponseDetailsDto.setCategoryName(CategoryName);
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
