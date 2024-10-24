package com.grad.ecommerce_ai.controller;

import com.grad.ecommerce_ai.utils.CloudinaryManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api/images")
public class ImageController {
    private final CloudinaryManager cloudinaryManager;

    public ImageController(CloudinaryManager cloudinaryManager) {
        this.cloudinaryManager = cloudinaryManager;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@Valid @RequestBody MultipartFile imageFile) throws IOException {
        String response = cloudinaryManager.uploadImageOnCloud(imageFile);
        return ResponseEntity.ok(response);
    }
}

