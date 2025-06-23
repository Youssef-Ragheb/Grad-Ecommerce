package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Wishlist;
import com.grad.ecommerce_ai.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    final private WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    @GetMapping
    public ApiResponse<List<Wishlist>> getWishlist(@RequestHeader String token) {
        return wishlistService.getWishlist(token);
    }
    @PostMapping()
    public ApiResponse<Wishlist> addWishlist(@RequestBody Wishlist wishlist,@RequestHeader String token){
        return wishlistService.addWishlist(wishlist,token);
    }
    @DeleteMapping
    public ApiResponse<Boolean> delete(@RequestBody Wishlist wishlist,@RequestHeader String token){
        return wishlistService.deleteWishlist(wishlist,token);
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;  // For SQL databases

    @GetMapping("/api/db-latency")
    public String checkDbLatency() {
        Instant start = Instant.now();

        // Execute a lightweight query (e.g., SELECT 1 for SQL, or a ping for MongoDB)
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        Instant end = Instant.now();
        long latencyMs = Duration.between(start, end).toMillis();

        return "Database latency: " + latencyMs + " ms";
    }
}
