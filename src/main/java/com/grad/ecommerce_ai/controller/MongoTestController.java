package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class MongoTestController {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DrugService drugService;

    @GetMapping("/test-mongo-connection")
    public String testMongoConnection() {
        long startTime = System.currentTimeMillis();
        mongoTemplate.executeCommand("{ ping: 1 }"); // Simple MongoDB ping
        long latency = System.currentTimeMillis() - startTime;
        return "MongoDB connection latency: " + latency + " ms";
    }
    @GetMapping("/clear/redis")
        public String clearRedis() {
        drugService.clearAllDrugCache();
        return "clear redis";
    }

}
