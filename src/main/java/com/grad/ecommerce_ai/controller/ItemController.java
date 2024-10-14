package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Item;
import com.grad.ecommerce_ai.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Save a new item and add it to the cart
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Item>> saveItem(@RequestBody Item item, @RequestHeader("Authorization") String token) {
        ApiResponse<Item> response = itemService.save(item, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Update an existing item
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Item>> updateItem(@PathVariable String id, @RequestBody Item item, @RequestHeader("Authorization") String token) {
        ApiResponse<Item> response = itemService.updateItem(id, item, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
