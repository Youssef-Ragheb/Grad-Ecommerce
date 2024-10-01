package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.service.MainDrugService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main/drug")
public class MainDrugController {
    private final MainDrugService mainDrugsService;

    public MainDrugController(MainDrugService mainDrugsService) {
        this.mainDrugsService = mainDrugsService;
    }

    @PostMapping
    public Drugs addDrug(Drugs drugs) {
        return mainDrugsService.addDrug(drugs);
    }
}
