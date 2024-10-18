package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.ActiveIngredient;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.repository.ActiveIngredientRepository;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActiveIngredientService {
    private final ActiveIngredientRepository activeIngredientRepository;
    private final MainDrugRepository mainDrugRepository;

    public ActiveIngredientService(ActiveIngredientRepository activeIngredientRepository, MainDrugRepository mainDrugRepository) {
        this.activeIngredientRepository = activeIngredientRepository;
        this.mainDrugRepository = mainDrugRepository;
    }

    public ApiResponse<List<ActiveIngredient>> getAllActiveIngredients() {
        ApiResponse<List<ActiveIngredient>> apiResponse = new ApiResponse<>();
        apiResponse.setData(activeIngredientRepository.findAll());
        apiResponse.setMessage("Active Ingredient List");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredient> getActiveIngredientById(String id) {
        ApiResponse<ActiveIngredient> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredient = activeIngredientRepository.findById(id);
        if (activeIngredient.isPresent()) {
            apiResponse.setData(activeIngredient.get());
            apiResponse.setMessage("Active Ingredient found");
            apiResponse.setStatus(true);
            apiResponse.setStatusCode(200);
            return apiResponse;
        }
        apiResponse.setData(null);
        apiResponse.setMessage("Active Ingredient not found");
        apiResponse.setStatus(false);
        apiResponse.setStatusCode(404);
        return apiResponse;
    }

    public ApiResponse<List<ActiveIngredient>> getActiveIngredientByName(String name) {
        ApiResponse<List<ActiveIngredient>> apiResponse = new ApiResponse<>();
        try{
            List<ActiveIngredient> activeIngredientList = activeIngredientRepository.findByActiveIngredientContainingIgnoreCase(name);
            System.out.println(activeIngredientList);
            apiResponse.setData(activeIngredientList);
            apiResponse.setMessage("Active Ingredient found");
            apiResponse.setStatus(true);
            apiResponse.setStatusCode(200);
            return apiResponse;
        }catch (Exception e){
            e.printStackTrace();  // This will log the exception in the console
            apiResponse.setData(null);
            apiResponse.setMessage("Active Ingredient not found: " + e.getMessage());
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(400);
            return apiResponse;
        }

    }

    public ApiResponse<Boolean> deleteActiveIngredientById(String id) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredient = activeIngredientRepository.findById(id);
        if (activeIngredient.isPresent()) {
            activeIngredientRepository.delete(activeIngredient.get());
            apiResponse.setData(true);
            apiResponse.setMessage("Active Ingredient deleted");
            apiResponse.setStatus(true);
            apiResponse.setStatusCode(200);
            return apiResponse;
        }
        apiResponse.setData(null);
        apiResponse.setMessage("Active Ingredient not found");
        apiResponse.setStatus(false);
        apiResponse.setStatusCode(404);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredient> saveActiveIngredient(ActiveIngredient activeIngredient) {
        ApiResponse<ActiveIngredient> apiResponse = new ApiResponse<>();
        apiResponse.setData(activeIngredientRepository.save(activeIngredient));
        apiResponse.setMessage("Active Ingredient saved");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredient> updateActiveIngredient(ActiveIngredient activeIngredient) {
        ApiResponse<ActiveIngredient> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredientOptional = activeIngredientRepository.findById(activeIngredient.getId());
        if (activeIngredientOptional.isPresent()) {
        if(activeIngredient.getDescription()== null || activeIngredient.getDescription().isEmpty() ){
            activeIngredient.setDescription(activeIngredientOptional.get().getDescription());
        }

            activeIngredientRepository.save(activeIngredient);
            apiResponse.setData(activeIngredientOptional.get());
            apiResponse.setMessage("Active Ingredient updated");
            apiResponse.setStatus(true);
            apiResponse.setStatusCode(200);
            return apiResponse;
        }
        apiResponse.setData(null);
        apiResponse.setMessage("Active Ingredient not found");
        apiResponse.setStatus(false);
        apiResponse.setStatusCode(404);
        return apiResponse;
    }

    public ApiResponse<Boolean> checkDrugActiveIngredient(String activeIngredientId, String DrugId) {

        ApiResponse<Boolean> response = new ApiResponse<>();
        Optional<Drugs> mainDrugs = mainDrugRepository.findById(DrugId);
        if (mainDrugs.isEmpty()) {
            response.setData(false);
            response.setMessage("drug not found");
            response.setStatus(false);
            response.setStatusCode(404);
            return response;
        }

        if (mainDrugs.get().getActiveIngredientId().equals(activeIngredientId)) {
            response.setData(true);
            response.setMessage("Active Ingredient is right");
            response.setStatus(true);
            response.setStatusCode(200);
            return response;
        }

        response.setData(false);
        response.setMessage("Active Ingredient Does not match");
        response.setStatus(false);
        response.setStatusCode(500);
        return response;
    }
}
