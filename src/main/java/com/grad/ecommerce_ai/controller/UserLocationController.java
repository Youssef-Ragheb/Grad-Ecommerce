package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.UserLocation;
import com.grad.ecommerce_ai.service.UserLocationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "http://localhost:3000")
public class UserLocationController {
    private final UserLocationService userLocationService;
    public UserLocationController(UserLocationService userLocationService){
        this.userLocationService = userLocationService;
    }

    @GetMapping("/get")
    public ApiResponse<UserLocation> getUserLocation(@RequestHeader String token){
        return userLocationService.getUserLocation(token);
    }
    @PostMapping("/set")
    public ApiResponse<UserLocation> setUserLocation (@RequestHeader String  token,
                                                      @RequestParam double lat,
                                                      @RequestParam  double lng){
        return userLocationService.addUserLocation(token, lat, lng);

    }

}
