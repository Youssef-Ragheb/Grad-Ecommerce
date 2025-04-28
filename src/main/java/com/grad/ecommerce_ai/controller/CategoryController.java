package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.CategoryDTO;
import com.grad.ecommerce_ai.entity.Category;
import com.grad.ecommerce_ai.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@RequestBody CategoryDTO category,
                                                                   @RequestHeader String token) {
        ApiResponse<CategoryDTO> response = categoryService.createCategory(category, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(@PathVariable Long id) {
        ApiResponse<CategoryDTO> response = categoryService.getCategory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        ApiResponse<List<CategoryDTO>> response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Update a category
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long id,
                                                                @RequestBody CategoryDTO updatedCategory,
                                                                @RequestHeader String token) {
        ApiResponse<CategoryDTO> response = categoryService.updateCategory(id, updatedCategory, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id,
                                                            @RequestHeader String token) {
        ApiResponse<Void> response = categoryService.deleteCategory(id, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
