package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Drugs;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import com.grad.ecommerce_ai.repository.InventoryDrugRepository;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryDrugService {
    private final InventoryDrugRepository inventoryDrugRepository;
    private final JwtService jwtService;
    private final CheckAuth checkAuth;
    private final DrugService drugService;
    private final RedisTemplate<String, InventoryDrug> redisTemplate;
    private static final String INVENTORY_KEY_PREFIX = "INVENTORY:"; // + inventoryId
    private static final String INVENTORY_BRANCH_KEY = "INVENTORY:BRANCH:"; // + branchId
    private static final String INVENTORY_DRUG_KEY = "INVENTORY:DRUG:"; // + drugId
    private static final String INVENTORY_DRUGS_KEY = "INVENTORY:DRUGS"; // optional for full list

    public InventoryDrugService(InventoryDrugRepository inventoryDrugRepository, JwtService jwtService, CheckAuth checkAuth, DrugService drugService, RedisTemplate<String, InventoryDrug> redisTemplate) {
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.jwtService = jwtService;
        this.checkAuth = checkAuth;
        this.drugService = drugService;
        this.redisTemplate = redisTemplate;
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
        Optional<Drugs> drug = drugService.findDrug(inventoryDrug.getDrugId());
        if(drug.isEmpty()){
            response.setStatus(false);
            response.setMessage("drug not found");
            response.setStatusCode(404);
            return response;
        }
        inventoryDrug.setActiveIngredientId(drug.get().getActiveIngredientId());
        inventoryDrug.setCategoryId(drug.get().getCategoryId());
        inventoryDrug.setDrugName(drug.get().getDrugName());
        response.setStatus(true);
        response.setMessage("success");
        response.setStatusCode(200);
        response.setData(saveInventory(inventoryDrug));
        return response;
    }

    public ApiResponse<InventoryDrug> getInventoryDrugById(String id, String token) {
        ApiResponse<InventoryDrug> response = new ApiResponse<>();
        try {
            InventoryDrug inventoryDrug = findInventoryById(id)
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
            List<InventoryDrug> inventoryDrugs = findByBranchId(branchId);
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
            InventoryDrug inventoryDrug = findInventoryById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, inventoryDrug.getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }

            deleteInventory(inventoryDrug.getId());
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
            InventoryDrug existingInventoryDrug = findInventoryById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Inventory Drug not found"));

            // Check user authorization for branch access
            Long userId = jwtService.extractUserId(token);
            if (!checkAuth.checkAuthToBranch(userId, existingInventoryDrug.getBranchId())) {
                response.setStatus(false);
                response.setMessage("unauthorized");
                response.setStatusCode(401);
                return response;
            }
            Optional<Drugs> drug = drugService.findDrug(existingInventoryDrug.getDrugId());
            if(drug.isEmpty()){
                response.setStatus(false);
                response.setStatusCode(404);
                response.setMessage("drug not found ");
                return response;
            }

            // Update the fields of the existing inventory drug
            existingInventoryDrug.setDrugId(inventoryDrug.getDrugId());
            existingInventoryDrug.setDrugName(drug.get().getDrugName());
            existingInventoryDrug.setCategoryId(drug.get().getCategoryId());
            existingInventoryDrug.setActiveIngredientId(drug.get().getActiveIngredientId());
            existingInventoryDrug.setPrice(inventoryDrug.getPrice());
            existingInventoryDrug.setStock(inventoryDrug.getStock());
            existingInventoryDrug.setBranchId(inventoryDrug.getBranchId());

            // Save the updated inventory drug
            InventoryDrug updatedInventoryDrug = saveInventory(existingInventoryDrug);

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
    public InventoryDrug saveInventory(InventoryDrug inventoryDrug) {
        InventoryDrug saved = inventoryDrugRepository.save(inventoryDrug);

        // Cache single object
        String singleKey = INVENTORY_KEY_PREFIX + saved.getId();
        redisTemplate.opsForValue().set(singleKey, saved);

        // Invalidate related lists
        redisTemplate.delete(INVENTORY_BRANCH_KEY + saved.getBranchId());
        redisTemplate.delete(INVENTORY_DRUG_KEY + saved.getDrugId());

        return saved;
    }
    public void deleteInventory(String id) {
        Optional<InventoryDrug> invOpt = inventoryDrugRepository.findById(id);
        invOpt.ifPresent(drug -> {
            redisTemplate.delete(INVENTORY_KEY_PREFIX + id);
            redisTemplate.delete(INVENTORY_BRANCH_KEY + drug.getBranchId());
            redisTemplate.delete(INVENTORY_DRUG_KEY + drug.getDrugId());
        });

        inventoryDrugRepository.deleteById(id);
    }
    public Optional<InventoryDrug> findInventoryById(String id) {
        String key = INVENTORY_KEY_PREFIX + id;
        InventoryDrug cached = redisTemplate.opsForValue().get(key);
        if (cached != null) return Optional.of(cached);

        Optional<InventoryDrug> db = inventoryDrugRepository.findById(id);
        db.ifPresent(inv -> redisTemplate.opsForValue().set(key, inv, Duration.ofMinutes(5)));
        return db;
    }
    public List<InventoryDrug> findByBranchId(Long branchId) {
        String key = INVENTORY_BRANCH_KEY + branchId;
        List<InventoryDrug> cached = redisTemplate.opsForList().range(key, 0, -1);
        if (cached != null && !cached.isEmpty()) return cached;

        List<InventoryDrug> dbList = inventoryDrugRepository.findAllByBranchId(branchId);
        if (!dbList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, dbList);
            redisTemplate.expire(key, Duration.ofMinutes(5));
        }
        return dbList;
    }

    public List<InventoryDrug> findByDrugIdIn(List<String> drugIds) {
        return inventoryDrugRepository.findAllByDrugIdIn(drugIds);
    }

    public List<InventoryDrug> findByDrugId(String drugId) {
        String key = INVENTORY_DRUG_KEY + drugId;
        List<InventoryDrug> cached = redisTemplate.opsForList().range(key, 0, -1);
        if (cached != null && !cached.isEmpty()) return cached;

        List<InventoryDrug> dbList = inventoryDrugRepository.findAllByDrugId(drugId);
        if (!dbList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, dbList);
            redisTemplate.expire(key, Duration.ofMinutes(5));
        }
        return dbList;
    }

    public void deleteInventoryByBranchId(Long branchId) {
        // Optional: Fetch first to get all IDs for clean cache invalidation
        List<InventoryDrug> list = inventoryDrugRepository.findAllByBranchId(branchId);
        for (InventoryDrug drug : list) {
            redisTemplate.delete(INVENTORY_KEY_PREFIX + drug.getId());
            redisTemplate.delete(INVENTORY_DRUG_KEY + drug.getDrugId());
        }

        redisTemplate.delete(INVENTORY_BRANCH_KEY + branchId);
        inventoryDrugRepository.deleteAllByBranchId(branchId);
    }




}
