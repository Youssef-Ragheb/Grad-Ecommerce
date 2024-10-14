package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Cart;
import com.grad.ecommerce_ai.enitity.Item;
import com.grad.ecommerce_ai.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
/*
    // Add an item to the cart
//    @PostMapping("/add")
//    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestBody Item item, @RequestHeader("Authorization") String token) {
//        ApiResponse<Cart> response = cartService.addToCart(item, token);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
*/
    // Remove an item from the cart
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Cart>> removeFromCart(@RequestBody Item item, @RequestHeader("Authorization") String token) {
        ApiResponse<Cart> response = cartService.removeFromCart(item, token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Get all items in the cart
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<Cart>> getItemsFromCart(@RequestHeader("Authorization") String token) {
        ApiResponse<Cart> response = cartService.getItemsFromCart(token);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

