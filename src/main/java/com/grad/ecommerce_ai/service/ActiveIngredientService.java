package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ActiveIngredientDTO;
import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.ActiveIngredient;
import com.grad.ecommerce_ai.repository.ActiveIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.activeIngredientDtoToActiveIngredient;
import static com.grad.ecommerce_ai.mappers.DtoConverter.activeIngredientToDto;

@Service
public class ActiveIngredientService {
    private final ActiveIngredientRepository activeIngredientRepository;


    public ActiveIngredientService(ActiveIngredientRepository activeIngredientRepository) {
        this.activeIngredientRepository = activeIngredientRepository;

    }

    public ApiResponse<List<ActiveIngredientDTO>> getAllActiveIngredients() {
        ApiResponse<List<ActiveIngredientDTO>> apiResponse = new ApiResponse<>();
        List<ActiveIngredient> activeIngredients = activeIngredientRepository.findAll();
        List<ActiveIngredientDTO> activeIngredientsDTO = new ArrayList<>();
        for (ActiveIngredient activeIngredient : activeIngredients) {
            activeIngredientsDTO.add(activeIngredientToDto(activeIngredient));
        }
        apiResponse.setData(activeIngredientsDTO);
        apiResponse.setMessage("Active Ingredient List");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredientDTO> getActiveIngredientById(Long id) {
        ApiResponse<ActiveIngredientDTO> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredient = activeIngredientRepository.findById(id);
        if (activeIngredient.isPresent()) {
            apiResponse.setData(activeIngredientToDto(activeIngredient.get()));
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

    public ApiResponse<List<ActiveIngredientDTO>> getActiveIngredientByName(String name) {
        ApiResponse<List<ActiveIngredientDTO>> apiResponse = new ApiResponse<>();
        try {
            List<ActiveIngredient> activeIngredientList = activeIngredientRepository.findAllByActiveIngredientNameContainingIgnoreCase(name);
            List<ActiveIngredientDTO> activeIngredientsDTO = new ArrayList<>();
            for (ActiveIngredient activeIngredient : activeIngredientList) {
                activeIngredientsDTO.add(activeIngredientToDto(activeIngredient));
            }
            apiResponse.setData(activeIngredientsDTO);
            apiResponse.setMessage("Active Ingredient found");
            apiResponse.setStatus(true);
            apiResponse.setStatusCode(200);
            return apiResponse;
        } catch (Exception e) {
            apiResponse.setData(null);
            apiResponse.setMessage("Active Ingredient not found: " + e.getMessage());
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(400);
            return apiResponse;
        }

    }

    public ApiResponse<Boolean> deleteActiveIngredientById(Long id) {
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

    public ApiResponse<ActiveIngredientDTO> saveActiveIngredient(ActiveIngredientDTO activeIngredientDTO) {
        ApiResponse<ActiveIngredientDTO> apiResponse = new ApiResponse<>();
        ActiveIngredient activeIngredient = activeIngredientRepository.save(activeIngredientDtoToActiveIngredient(activeIngredientDTO));
        apiResponse.setData(activeIngredientToDto(activeIngredient));
        apiResponse.setMessage("Active Ingredient saved");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredientDTO> updateActiveIngredient(ActiveIngredientDTO activeIngredientDTO) {
        ApiResponse<ActiveIngredientDTO> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredientOptional = activeIngredientRepository.findById(activeIngredientDTO.getId());
        if (activeIngredientOptional.isPresent()) {
            if (activeIngredientDTO.getDescription() == null || activeIngredientDTO.getDescription().isEmpty()) {
                activeIngredientDTO.setDescription(activeIngredientOptional.get().getDescription());
            }

            ActiveIngredient activeIngredient = activeIngredientRepository.save(activeIngredientDtoToActiveIngredient(activeIngredientDTO));
            apiResponse.setData(activeIngredientToDto(activeIngredient));
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


}
