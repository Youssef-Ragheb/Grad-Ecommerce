package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.UserLocation;
import com.grad.ecommerce_ai.repository.UserLocationRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLocationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserLocationRepository userLocationRepository;

    public UserLocationService(JwtService jwtService, UserRepository userRepository, UserLocationRepository userLocationRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userLocationRepository = userLocationRepository;
    }

    public ApiResponse<UserLocation> addUserLocation(String token, double lat, double lng){
        ApiResponse<UserLocation> response = new ApiResponse<>();

        Long userId = jwtService.extractUserId(token);

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            response.setMessage("User does not exist");
            response.setStatus(false);
            response.setStatusCode(404);
            return response;
        }
        Optional<UserLocation> optionalUserLocation = userLocationRepository.findByUserId(userId);
        if(optionalUserLocation.isEmpty()){
            UserLocation userLocation = new UserLocation();
            userLocation.setLng(lng);
            userLocation.setLat(lat);
            userLocation.setUserId(userId);
            response.setData(userLocationRepository.save(userLocation));
            response.setMessage("User Location Has been added ");
            response.setStatus(true);
            response.setStatusCode(201);
            return response;
        }
        UserLocation userLocation = optionalUserLocation.get();
        userLocation.setLng(lng);
        userLocation.setLat(lat);
        response.setData(userLocationRepository.save(userLocation));
        response.setMessage("User Has updated his location ");
        response.setStatus(true);
        response.setStatusCode(200);
        return response;

    }
    public ApiResponse<UserLocation> getUserLocation(String token){
        ApiResponse<UserLocation> response = new ApiResponse<>();

        Long userId = jwtService.extractUserId(token);

        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            response.setMessage("User does not exist");
            response.setStatus(false);
            response.setStatusCode(404);
            return response;
        }
        Optional<UserLocation> optionalUserLocation = userLocationRepository.findByUserId(userId);
        if(optionalUserLocation.isEmpty()){
            response.setMessage("User does not has location set");
            response.setStatus(false);
            response.setStatusCode(404);
            return response;
        }
        response.setData(optionalUserLocation.get());
        response.setMessage("User Has updated his location ");
        response.setStatus(true);
        response.setStatusCode(200);
        return response;
    }

    public UserLocation getLocationByUserId(Long userId){
        return userLocationRepository.findByUserId(userId).orElseThrow();
    }
}
