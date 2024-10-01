package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.enitity.DrugResponse;
import com.grad.ecommerce_ai.service.MainDrugService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
public class FetchController {
    private final MainDrugService mainDrugService;

    public FetchController(MainDrugService mainDrugService) {
        this.mainDrugService = mainDrugService;
    }

    @GetMapping
    public DrugResponse fetch() {
       return mainDrugService.fetchDrugs();

    }
}
