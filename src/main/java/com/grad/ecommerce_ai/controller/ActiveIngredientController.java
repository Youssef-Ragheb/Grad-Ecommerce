package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ActiveIngredientDTO;
import com.grad.ecommerce_ai.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<ActiveIngredientDTO>>> getAllActiveIngredients() {
        ApiResponse<List<ActiveIngredientDTO>> response = activeIngredientService.getAllActiveIngredients();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActiveIngredientDTO>> getActiveIngredientById(@PathVariable Long id) {
        ApiResponse<ActiveIngredientDTO> response = activeIngredientService.getActiveIngredientById(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ActiveIngredientDTO>>> getActiveIngredientByName(@PathVariable String name) {
        ApiResponse<List<ActiveIngredientDTO>> response = activeIngredientService.getActiveIngredientByName(name);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ActiveIngredientDTO>> saveActiveIngredient(@RequestBody ActiveIngredientDTO activeIngredient) {
        ApiResponse<ActiveIngredientDTO> response = activeIngredientService.saveActiveIngredient(activeIngredient);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActiveIngredientDTO>> updateActiveIngredient(@PathVariable Long id, @RequestBody ActiveIngredientDTO activeIngredientDTO) {
        activeIngredientDTO.setId(id);
        ApiResponse<ActiveIngredientDTO> response = activeIngredientService.updateActiveIngredient(activeIngredientDTO);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteActiveIngredientById(@PathVariable Long id) {
        ApiResponse<Boolean> response = activeIngredientService.deleteActiveIngredientById(id);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }
}
