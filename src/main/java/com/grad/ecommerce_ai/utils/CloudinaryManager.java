package com.grad.ecommerce_ai.utils;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class CloudinaryManager {
    private final Cloudinary cloudinary;

    public CloudinaryManager(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public String uploadImageOnCloud(MultipartFile imageFile) {
        try {
            Map uploadResult = cloudinary.uploader()
                    .upload(imageFile.getBytes(), ObjectUtils.asMap("public_id", UUID.randomUUID().toString()));
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            e.printStackTrace(); // Better: use a logger
            return "failure: " + e.getMessage();
        }
    }
//    public Boolean deleteImageOnCloud(String imageUrl) throws IOException {
//        String imageURL = cloudinary.uploader()
//                .upload(imageFile.getBytes(), ObjectUtils.asMap("public_id", UUID.randomUUID().toString()))
//                .get("url")
//                .toString();
//        if (!imageURL.isBlank()) {
//            return imageURL;
//        }
//        return "failure";
//    }

}
