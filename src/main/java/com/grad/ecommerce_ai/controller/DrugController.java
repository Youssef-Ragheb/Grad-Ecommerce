package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.service.DrugService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/drugs")
public class DrugController {


    private final DrugService drugsService;

    public DrugController(DrugService drugsService) {
        this.drugsService = drugsService;
    }

    @GetMapping("/{id}")
    public ApiResponse<Drugs> getDrugById(@PathVariable String id) {
        return drugsService.getDrugById(id);
    }

    @GetMapping
    public ApiResponse<List<Drugs>> getAllDrugs() {
        return drugsService.getAllDrugs();
    }

    // New endpoint to search drugs by name
    @GetMapping("/search")
    public ApiResponse<List<Drugs>> searchDrugsByName(@RequestParam("name") String name) {
        return drugsService.searchDrugsByName(name);
    }

    @PutMapping("/{id}")
    public ApiResponse<Drugs> updateDrug(@PathVariable String id, @RequestBody Drugs updatedDrug, @RequestHeader("Authorization") String token) {
        return drugsService.updateDrug(id, updatedDrug, token);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDrug(@PathVariable String id, @RequestHeader("Authorization") String token) {
        return drugsService.deleteDrug(id, token);
    }
}


