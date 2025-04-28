package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.DrugDTO;
import com.grad.ecommerce_ai.service.DrugService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/drugs")
public class DrugController {


    private final DrugService drugsService;

    public DrugController(DrugService drugsService) {
        this.drugsService = drugsService;
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DrugDTO>> addDrugToMain(@RequestBody DrugDTO drug, @RequestHeader String token) {
        ApiResponse<DrugDTO> apiResponse = drugsService.addDrugToMain(drug, token);

        // Check the status of the ApiResponse to return the appropriate HTTP status code
        if (!apiResponse.isStatus()) {
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiResponse<DrugDTO> getDrugById(@PathVariable Long id) {
        return drugsService.getDrugById(id);
    }

    @GetMapping
    public ApiResponse<List<DrugDTO>> getAllDrugs() {
        return drugsService.getAllDrugs();
    }

    // New endpoint to search drugs by name
    @GetMapping("/search")
    public ApiResponse<List<DrugDTO>> searchDrugsByName(@RequestParam("name") String name) {
        return drugsService.searchDrugsByName(name);
    }

    @PutMapping("/{id}")
    public ApiResponse<DrugDTO> updateDrug(@PathVariable Long id, @RequestBody DrugDTO updatedDrug, @RequestHeader String token) {
        return drugsService.updateDrug(id, updatedDrug, token);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDrug(@PathVariable Long id, @RequestHeader String token) {
        return drugsService.deleteDrug(id, token);
    }
}


