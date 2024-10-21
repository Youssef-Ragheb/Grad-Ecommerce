package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.UserRoles;
import com.grad.ecommerce_ai.enitity.Wishlist;
import com.grad.ecommerce_ai.repository.MainDrugRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import com.grad.ecommerce_ai.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MainDrugRepository mainDrugRepository;
    private final WishlistRepository wishlistRepository;

    public WishlistService (JwtService jwtService, UserRepository userRepository, MainDrugRepository mainDrugRepository, WishlistRepository wishlistRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.mainDrugRepository = mainDrugRepository;
        this.wishlistRepository = wishlistRepository;
    }

    public ApiResponse<Wishlist> addWishlist(Wishlist wishlist, String token) {
        ApiResponse<Wishlist> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if (!user.get().getUserRoles().equals(UserRoles.ROLE_CLIENT)) {
            apiResponse.setMessage("User is not client");
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        String drugId = wishlist.getDrugId();
        if (!mainDrugRepository.existsById(drugId)) {
            apiResponse.setMessage("drug not found");
            apiResponse.setStatusCode(404);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Wishlist wishlistEntity = new Wishlist();
        wishlistEntity.setUserId(userId);
        wishlistEntity.setDrugId(drugId);
        apiResponse.setData(wishlistRepository.save(wishlistEntity));
        apiResponse.setMessage("wishlist saved");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        return apiResponse;
    }

    public ApiResponse<List<Wishlist>> getWishlist(String token) {
        ApiResponse<List<Wishlist>> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if (!user.get().getUserRoles().equals(UserRoles.ROLE_CLIENT)) {
            apiResponse.setMessage("User is not client");
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        apiResponse.setData(wishlistRepository.findByUserId(userId));
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setMessage("wishlist found");
        return apiResponse;
    }

    public ApiResponse<Boolean> deleteWishlist(Wishlist wishlist, String token) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if (!user.get().getUserRoles().equals(UserRoles.ROLE_CLIENT)) {
            apiResponse.setMessage("User is not client");
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        if(!wishlistRepository.existsById(wishlist.getId())) {
            apiResponse.setData(false);
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            apiResponse.setMessage("wishlist not found");
            return apiResponse;
        }
        wishlistRepository.deleteById(wishlist.getId());
        apiResponse.setData(true);
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setMessage("wishlist deleted");
        return apiResponse;
    }
}
