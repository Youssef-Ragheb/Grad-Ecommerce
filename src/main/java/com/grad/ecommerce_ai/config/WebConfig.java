//package com.grad.ecommerce_ai.config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins(
//                        "http://grad-ecommerce-production.up.railway.app",
//                        "http://localhost:3000",  // React or frontend running locally
//                        "http://localhost:8080"   // Swagger UI (if running locally)
//                )
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true);  // Enable cookies or tokens to be sent
//    }
//}
