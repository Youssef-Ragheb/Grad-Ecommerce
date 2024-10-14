package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Category;
import com.grad.ecommerce_ai.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category,
                                                                @RequestHeader("Authorization") String token) {
        // Call service to create the category
        ApiResponse<Category> response = categoryService.createCategory(category, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get a category by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategory(@PathVariable String id) {
        ApiResponse<Category> response = categoryService.getCategory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        ApiResponse<List<Category>> response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Update a category
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable String id,
                                                                @RequestBody Category updatedCategory,
                                                                @RequestHeader("Authorization") String token) {
        ApiResponse<Category> response = categoryService.updateCategory(id, updatedCategory, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id,
                                                            @RequestHeader("Authorization") String token) {
        ApiResponse<Void> response = categoryService.deleteCategory(id, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
