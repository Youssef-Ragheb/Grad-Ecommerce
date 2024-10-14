package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.ActiveIngredient;
import com.grad.ecommerce_ai.service.ActiveIngredientService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/activeIngredient")
public class ActiveIngredientController {

    private final ActiveIngredientService activeIngredientService;

    public ActiveIngredientController(ActiveIngredientService activeIngredientService) {
        this.activeIngredientService = activeIngredientService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActiveIngredient>>> getAllActiveIngredients() {
        ApiResponse<List<ActiveIngredient>> response = activeIngredientService.getAllActiveIngredients();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActiveIngredient>> getActiveIngredientById(@PathVariable String id) {
        ApiResponse<ActiveIngredient> response = activeIngredientService.getActiveIngredientById(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<ActiveIngredient>> getActiveIngredientByName(@PathVariable String name) {
        ApiResponse<ActiveIngredient> response = activeIngredientService.getActiveIngredientByName(name);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ActiveIngredient>> saveActiveIngredient(@RequestBody ActiveIngredient activeIngredient) {
        ApiResponse<ActiveIngredient> response = activeIngredientService.saveActiveIngredient(activeIngredient);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActiveIngredient>> updateActiveIngredient(
            @PathVariable String id, @RequestBody ActiveIngredient activeIngredient) {
        activeIngredient.setId(id);
        ApiResponse<ActiveIngredient> response = activeIngredientService.updateActiveIngredient(activeIngredient);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteActiveIngredientById(@PathVariable String id) {
        ApiResponse<Boolean> response = activeIngredientService.deleteActiveIngredientById(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
}
