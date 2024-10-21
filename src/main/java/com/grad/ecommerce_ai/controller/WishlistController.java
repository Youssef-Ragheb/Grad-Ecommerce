package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Wishlist;
import com.grad.ecommerce_ai.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin(origins = "http://localhost:3000")
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
}
