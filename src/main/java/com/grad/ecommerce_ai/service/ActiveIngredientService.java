package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.ActiveIngredient;
import com.grad.ecommerce_ai.entity.Drugs;
import com.grad.ecommerce_ai.repository.ActiveIngredientRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActiveIngredientService {
    private static final String ACTIVE_KEY_PREFIX = "ACTIVE:";
    private static final String ACTIVE_ALL_KEY = "ACTIVE:ALL";
    private static final String ACTIVE_NAME_PREFIX = "ACTIVE:NAME:";
    private final ActiveIngredientRepository activeIngredientRepository;
    private final DrugService drugService;
    private final RedisTemplate<String, ActiveIngredient> redisTemplate;

    public ActiveIngredientService(ActiveIngredientRepository activeIngredientRepository, DrugService drugService, RedisTemplate<String, ActiveIngredient> redisTemplate) {
        this.activeIngredientRepository = activeIngredientRepository;
        this.drugService = drugService;
        this.redisTemplate = redisTemplate;
    }

    public ApiResponse<List<ActiveIngredient>> getAllActiveIngredients() {
        ApiResponse<List<ActiveIngredient>> apiResponse = new ApiResponse<>();
        apiResponse.setData(findAllActive());
        apiResponse.setMessage("Active Ingredient List");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredient> getActiveIngredientById(String id) {
        ApiResponse<ActiveIngredient> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredient = findActiveIngredientById(id);
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
        try {
            List<ActiveIngredient> activeIngredientList = findActiveIngredientByName(name);
            System.out.println(activeIngredientList);
            apiResponse.setData(activeIngredientList);
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

    public ApiResponse<Boolean> deleteActiveIngredientById(String id) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredient = findActiveIngredientById(id);
        if (activeIngredient.isPresent()) {
            deleteActiveIngredient(activeIngredient.get().getId());
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
        apiResponse.setData(saveActive(activeIngredient));
        apiResponse.setMessage("Active Ingredient saved");
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    public ApiResponse<ActiveIngredient> updateActiveIngredient(ActiveIngredient activeIngredient) {
        ApiResponse<ActiveIngredient> apiResponse = new ApiResponse<>();
        Optional<ActiveIngredient> activeIngredientOptional = findActiveIngredientById(activeIngredient.getId());
        if (activeIngredientOptional.isPresent()) {
            if (activeIngredient.getDescription() == null || activeIngredient.getDescription().isEmpty()) {
                activeIngredient.setDescription(activeIngredientOptional.get().getDescription());
            }

            saveActive(activeIngredient);
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
        Optional<Drugs> mainDrugs = drugService.findDrug(DrugId);
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

    public Optional<ActiveIngredient> findActiveIngredientById(String id) {
        String key = ACTIVE_KEY_PREFIX + id;
        ActiveIngredient activeIngredient = redisTemplate.opsForValue().get(key);
        if (activeIngredient != null) {
            return Optional.of(activeIngredient);
        }
        Optional<ActiveIngredient> activeIngredientOptional = activeIngredientRepository.findById(id);
        activeIngredientOptional.ifPresent(d -> redisTemplate.opsForValue().set(key, d));
        return activeIngredientOptional;
    }

    public List<ActiveIngredient> findActiveIngredientByName(String name) {
        String key = ACTIVE_NAME_PREFIX + name.toLowerCase();

        // 1. Try Redis first
        List<ActiveIngredient> cachedList = redisTemplate.opsForList().range(key, 0, -1);
        if (cachedList != null && !cachedList.isEmpty()) {
            return cachedList;
        }

        // 2. Fetch from DB
        List<ActiveIngredient> dbList = activeIngredientRepository.findByActiveIngredientContainingIgnoreCase(name);

        // 3. Cache the result if not empty
        if (!dbList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, dbList);
        }

        // 4. Return result
        return dbList;
    }

    public ActiveIngredient saveActive(ActiveIngredient activeIngredient) {
        ActiveIngredient saved = activeIngredientRepository.save(activeIngredient);

        // Cache single item
        String singleKey = ACTIVE_KEY_PREFIX + saved.getId();
        redisTemplate.opsForValue().set(singleKey, saved);

        // Invalidate list caches
        redisTemplate.delete(ACTIVE_ALL_KEY); // findAll()
        // Optionally, delete all name-based keys if you keep a pattern (advanced)

        return saved;
    }

    public List<ActiveIngredient> findAllActive() {
        // Try cache first
        List<ActiveIngredient> cachedList = redisTemplate.opsForList().range(ACTIVE_ALL_KEY, 0, -1);
        if (cachedList != null && !cachedList.isEmpty()) {
            return cachedList;
        }

        // DB fallback
        List<ActiveIngredient> dbList = activeIngredientRepository.findAll();

        // Cache result
        if (!dbList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(ACTIVE_ALL_KEY, dbList);
        }

        return dbList;
    }

    public void deleteActiveIngredient(String id) {
        activeIngredientRepository.deleteById(id);

        // Remove from cache
        redisTemplate.delete(ACTIVE_KEY_PREFIX + id);
        redisTemplate.delete(ACTIVE_ALL_KEY);

    }
}
