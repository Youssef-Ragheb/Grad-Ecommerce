package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.enitity.DrugResponse;
import com.grad.ecommerce_ai.enitity.Drugs;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MainDrugService {
    private final MainDrugRepository mainDrugRepository;
    private final RestTemplate restTemplate;

    public MainDrugService(MainDrugRepository mainDrugRepository, RestTemplate restTemplate) {
        this.mainDrugRepository = mainDrugRepository;
        this.restTemplate = restTemplate;
    }

    public DrugResponse fetchDrugs() {
        String url = "https://dailymed.nlm.nih.gov/dailymed/services/v2/drugnames.json?pagesize=1&page=1";

//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//                .queryParam("pagesize", pageSize)
//                .queryParam("page", page);
        System.out.println(url);
        return restTemplate.getForObject(url, DrugResponse.class);
    }
    public Drugs addDrug(Drugs drugs) {
        return mainDrugRepository.save(drugs);
    }



}
