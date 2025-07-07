package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Drugs;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DrugService {
    private static final String DRUG_KEY_PREFIX = "DRUG:";
    private static final String DRUG_ALL_KEY = "DRUG:ALL";
    private static final String DRUG_CATEGORY_PREFIX = "DRUG:CATEGORY:";
    private final JwtService jwtService;
    private final MainDrugRepository mainDrugRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public DrugService(JwtService jwtService, MainDrugRepository mainDrugRepository, RedisTemplate<String, Object> redisTemplate) {
        this.jwtService = jwtService;
        this.mainDrugRepository = mainDrugRepository;

        this.redisTemplate = redisTemplate;
    }

    public ApiResponse<Drugs> addDrugToMain(Drugs drug, String token) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        if (!jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setData(null);
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        apiResponse.setData(saveDrug(drug));
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("drug added");
        return apiResponse;
    }

    // Read a single drug by ID
    public ApiResponse<Drugs> getDrugById(String id) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        Optional<Drugs> drugOpt = findDrug(id);
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
        List<Drugs> drugsList = findAllDrugs();
        apiResponse.setData(drugsList);
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("All drugs retrieved successfully");
        return apiResponse;
    }

    // Update an existing drug by ID
    public ApiResponse<Drugs> updateDrug(String id, Drugs updatedDrug, String token) {
        ApiResponse<Drugs> apiResponse = new ApiResponse<>();
        if (!jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setData(null);
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<Drugs> existingDrugOpt = findDrug(id);
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

        Drugs savedDrug = saveDrug(existingDrug);
        apiResponse.setData(savedDrug);
        apiResponse.setStatus(true);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Drug updated successfully");
        return apiResponse;
    }

    // Delete an existing drug by ID
    public ApiResponse<Void> deleteDrug(String id, String token) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        if (!jwtService.isAdmin(token)) {
            apiResponse.setMessage("unauthorized");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<Drugs> existingDrugOpt = findDrug(id);
        if (existingDrugOpt.isEmpty()) {
            apiResponse.setMessage("Drug not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        deleteDrug(id);
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


    public Optional<Drugs> findDrug(String id) {
        String key = DRUG_KEY_PREFIX + id;

        Object raw = redisTemplate.opsForValue().get(key);
        if (raw instanceof Drugs cachedDrug) {
            return Optional.of(cachedDrug);
        }

        Optional<Drugs> drug = mainDrugRepository.findById(id);
        drug.ifPresent(d -> redisTemplate.opsForValue().set(key, d));
        return drug;
    }

    public List<Drugs> findAllDrugs() {
        Object raw = redisTemplate.opsForValue().get(DRUG_ALL_KEY);
        if (raw instanceof List<?> list && (list.isEmpty() || list.get(0) instanceof Drugs)) {
            return (List<Drugs>) list;
        }

        List<Drugs> drugs = mainDrugRepository.findAll();
        redisTemplate.opsForValue().set(DRUG_ALL_KEY, drugs);
        return drugs;
    }

    public Drugs saveDrug(Drugs drug) {
        Drugs saved = mainDrugRepository.save(drug);

        String key = DRUG_KEY_PREFIX + saved.getId();
        redisTemplate.opsForValue().set(key, saved);
        redisTemplate.delete(DRUG_ALL_KEY);
        redisTemplate.delete(DRUG_CATEGORY_PREFIX + saved.getCategoryId());

        return saved;
    }

    public List<Drugs> findDrugsByCategory(String categoryId) {
        String cacheKey = DRUG_CATEGORY_PREFIX + categoryId;

        Object raw = redisTemplate.opsForValue().get(cacheKey);
        if (raw instanceof List<?> list && (list.isEmpty() || list.get(0) instanceof Drugs)) {
            return (List<Drugs>) list;
        }

        List<Drugs> drugs = mainDrugRepository.findByCategoryId(categoryId);
        if (!drugs.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, drugs);
        }

        return drugs;
    }
    public List<Drugs> findDrugsByIds(List<String> ids) {
        List<Drugs> result = new ArrayList<>();
        List<String> missingIds = new ArrayList<>();

        // Step 1: Try to get each drug from Redis
        for (String id : ids) {
            String key = DRUG_KEY_PREFIX + id;
            Object raw = redisTemplate.opsForValue().get(key);

            if (raw instanceof Drugs cachedDrug) {
                result.add(cachedDrug);
            } else {
                missingIds.add(id);
            }
        }

        // Step 2: Fetch missing ones from DB in one call
        if (!missingIds.isEmpty()) {
            List<Drugs> dbDrugs = mainDrugRepository.findAllById(missingIds);
            result.addAll(dbDrugs);

            // Step 3: Cache them back to Redis
            for (Drugs drug : dbDrugs) {
                String key = DRUG_KEY_PREFIX + drug.getId();
                redisTemplate.opsForValue().set(key, drug);
            }
        }

        return result;
    }
    public void deleteDrug(String id) {
        Optional<Drugs> drug = mainDrugRepository.findById(id);
        drug.ifPresent(d -> {
            redisTemplate.delete(DRUG_CATEGORY_PREFIX + d.getCategoryId());
        });

        mainDrugRepository.deleteById(id);
        redisTemplate.delete(DRUG_KEY_PREFIX + id);
        redisTemplate.delete(DRUG_ALL_KEY);
    }
    public void clearAllDrugCache() {
        Set<String> keys = redisTemplate.keys("DRUG:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

}

