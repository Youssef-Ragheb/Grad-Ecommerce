package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Category;
import com.grad.ecommerce_ai.repository.CategoryRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final String CATEGORY_KEY_PREFIX = "CATEGORY:";
    private static final String CATEGORY_ALL_KEY = "CATEGORY:ALL";
    private final CategoryRepository categoryRepository;
    private final JwtService jwtTokenUtil;
    private final RedisTemplate<String, Category> redisTemplate;

    public CategoryService(CategoryRepository categoryRepository, JwtService jwtTokenUtil, RedisTemplate<String, Category> redisTemplate) {
        this.categoryRepository = categoryRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
    }

    // Create a new category with token-based admin validation
    public ApiResponse<Category> createCategory(Category category, String token) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();

        // Check if the user is admin
//        if (!jwtTokenUtil.isAdmin(token)) {
//            apiResponse.setStatusCode(403);
//            apiResponse.setMessage("Access denied: only admins can create categories");
//            apiResponse.setStatus(false);
//            return apiResponse;
//        }

        // Check if category name already exists
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("Category name already exists");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Save the new category
        Category savedCategory = saveCategory(category);
        apiResponse.setData(savedCategory);
        apiResponse.setStatusCode(201);
        apiResponse.setMessage("Category created successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Get category by id
    public ApiResponse<Category> getCategory(String id) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();
        Optional<Category> categoryOpt = findByCategoryId(id);

        if (categoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        apiResponse.setData(categoryOpt.get());
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category retrieved successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    public ApiResponse<List<Category>> getAllCategories() {
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        List<Category> categoryList = findAllCategories();
        apiResponse.setData(categoryList);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category list retrieved successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Update category with token-based admin validation
    public ApiResponse<Category> updateCategory(String id, Category updatedCategory, String token) {
        ApiResponse<Category> apiResponse = new ApiResponse<>();

        // Check if the user is admin
        if (!jwtTokenUtil.isAdmin(token)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("Access denied: only admins can update categories");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check if category exists
        Optional<Category> existingCategoryOpt = findByCategoryId(id);
        if (existingCategoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Update the category fields
        Category existingCategory = existingCategoryOpt.get();
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        existingCategory.setLogo(updatedCategory.getLogo());

        // Save the updated category
        Category savedCategory = saveCategory(existingCategory);
        apiResponse.setData(savedCategory);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category updated successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Delete category with token-based admin validation
    public ApiResponse<Void> deleteCategory(String id, String token) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        // Check if the user is admin
        if (!jwtTokenUtil.isAdmin(token)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("Access denied: only admins can delete categories");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check if category exists
        Optional<Category> categoryOpt = findByCategoryId(id);
        if (categoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Delete the category
        deleteCategory(id);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category deleted successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Get category by ID with caching
    public Optional<Category> findByCategoryId(String id) {
        String key = CATEGORY_KEY_PREFIX + id;

        Category cached = redisTemplate.opsForValue().get(key);
        if (cached != null) return Optional.of(cached);

        Optional<Category> category = categoryRepository.findById(id);
        category.ifPresent(cat -> redisTemplate.opsForValue().set(key, cat));
        return category;
    }

    // Save category (update Redis and invalidate list)
    public Category saveCategory(Category category) {
        Category saved = categoryRepository.save(category);

        redisTemplate.opsForValue().set(CATEGORY_KEY_PREFIX + saved.getId(), saved);
        redisTemplate.delete(CATEGORY_ALL_KEY); // invalidate cached list
        return saved;
    }

    // Delete category (remove from cache)
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
        redisTemplate.delete(CATEGORY_KEY_PREFIX + id);
        redisTemplate.delete(CATEGORY_ALL_KEY);
    }

    // Get all categories with Redis caching
    public List<Category> findAllCategories() {
        List<Category> cached = redisTemplate.opsForList().range(CATEGORY_ALL_KEY, 0, -1);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<Category> categories = categoryRepository.findAll();
        redisTemplate.opsForList().rightPushAll(CATEGORY_ALL_KEY, categories);
        return categories;
    }
}
