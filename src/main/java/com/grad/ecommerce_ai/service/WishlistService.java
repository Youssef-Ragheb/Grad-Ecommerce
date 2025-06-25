package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.UserRoles;
import com.grad.ecommerce_ai.entity.Wishlist;
import com.grad.ecommerce_ai.repository.UserRepository;
import com.grad.ecommerce_ai.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final DrugService drugService;

    public WishlistService(JwtService jwtService, UserRepository userRepository, WishlistRepository wishlistRepository, DrugService drugService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.drugService = drugService;
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
        if (drugService.findDrug(drugId).isEmpty()) {
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
        Optional<User> user = userRepository.
                findById(jwtService.extractUserId(token));
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
        apiResponse.setData(wishlistRepository.findByUserId(user.get().getId()));
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setMessage("wishlist found");
        return apiResponse;
    }

    public ApiResponse<Boolean> deleteWishlist(Wishlist wishlist, String token) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        Optional<User> user = userRepository.findById(jwtService.extractUserId(token));
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
        if (!wishlistRepository.existsById(wishlist.getId())) {
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
