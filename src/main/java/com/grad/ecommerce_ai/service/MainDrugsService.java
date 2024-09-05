package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.repository.MainDrugRepository;
import org.springframework.stereotype.Service;

@Service
public class MainDrugsService {
    private final MainDrugRepository mainDrugRepository;

    public MainDrugsService(MainDrugRepository mainDrugRepository) {
        this.mainDrugRepository = mainDrugRepository;
    }
    //public ApiResponse
}
