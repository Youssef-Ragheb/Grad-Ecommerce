package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.CategoryDTO;
import com.grad.ecommerce_ai.entity.Category;
import com.grad.ecommerce_ai.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.categoryDtoToCategory;
import static com.grad.ecommerce_ai.mappers.DtoConverter.categoryToDto;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtService jwtTokenUtil;

    public CategoryService(CategoryRepository categoryRepository, JwtService jwtTokenUtil) {
        this.categoryRepository = categoryRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Create a new category with token-based admin validation
    public ApiResponse<CategoryDTO> createCategory(CategoryDTO category, String token) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();

        // Check if the user is admin
        if (!jwtTokenUtil.isAdmin(token)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("Access denied: only admins can create categories");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check if category name already exists
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("Category name already exists");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Save the new category
        Category savedCategory = categoryRepository.save(categoryDtoToCategory(category));

        apiResponse.setData(categoryToDto(savedCategory));
        apiResponse.setStatusCode(201);
        apiResponse.setMessage("Category created successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Get category by id
    public ApiResponse<CategoryDTO> getCategory(Long id) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();
        Optional<Category> categoryOpt = categoryRepository.findById(id);

        if (categoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        apiResponse.setData(categoryToDto(categoryOpt.get()));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category retrieved successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }
    public ApiResponse<List<CategoryDTO>> getAllCategories(){
        ApiResponse<List<CategoryDTO>> apiResponse = new ApiResponse<>();
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = categoryToDto(category);
            categoryDTOList.add(categoryDTO);
        }
        apiResponse.setData(categoryDTOList);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category list retrieved successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Update category with token-based admin validation
    public ApiResponse<CategoryDTO> updateCategory(Long id, CategoryDTO updatedCategory, String token) {
        ApiResponse<CategoryDTO> apiResponse = new ApiResponse<>();

        // Check if the user is admin
        if (!jwtTokenUtil.isAdmin(token)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("Access denied: only admins can update categories");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check if category exists
        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
        if (existingCategoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Update the category fields
        Category existingCategory = existingCategoryOpt.get();
        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        existingCategory.setLogo(updatedCategory.getLogoUrl());

        // Save the updated category
        Category savedCategory = categoryRepository.save(existingCategory);
        apiResponse.setData(categoryToDto(savedCategory));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category updated successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }

    // Delete category with token-based admin validation
    public ApiResponse<Void> deleteCategory(Long id, String token) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();

        // Check if the user is admin
        if (!jwtTokenUtil.isAdmin(token)) {
            apiResponse.setStatusCode(403);
            apiResponse.setMessage("Access denied: only admins can delete categories");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check if category exists
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("Category not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Delete the category
        categoryRepository.deleteById(id);
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("Category deleted successfully");
        apiResponse.setStatus(true);
        return apiResponse;
    }
}
