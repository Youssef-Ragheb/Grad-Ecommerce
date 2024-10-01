package com.grad.ecommerce_ai.enitity;

import com.grad.ecommerce_ai.service.MainDrugService;
import lombok.Data;

import java.util.List;
@Data
public class DrugResponse {
    private List<DrugData> data;
    private Metadata metadata;

    // getters and setters
}