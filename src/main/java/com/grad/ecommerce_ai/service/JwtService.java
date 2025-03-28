package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.dto.enums.UserRoles;
import com.grad.ecommerce_ai.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private final long jwtExpirationInMs; // 100h
    private final String secretKey;
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        secretKey = "WQzX+qet+e0vCZBrZaXgCgCIfEPdI3Inb4368Aht/Ls=";
        jwtExpirationInMs = 1000 * 60 * 60 * 100;
        this.userRepository = userRepository;
    }

    /*
    //    public String generateSecretKey() {
    //        try {
    //            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    //            SecretKey secretKey = keyGen.generateKey();
    //            String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    //            System.out.println("key: " + key);
    //            return key;
    //        } catch (NoSuchAlgorithmException e) {
    //            throw new RuntimeException("Error generating secret key", e);
    //        }
    //    }
    */
    public String generateToken(String Username, Long id, UserRoles userRoles) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        claims.put("role", userRoles.name());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String token = Jwts.builder().addClaims(claims).setSubject(Username).setIssuedAt(now).setExpiration(expiryDate).signWith(getKey(), SignatureAlgorithm.HS256).compact();
        System.out.println("Generated JWT Token: " + token); // Debug log
        return token;
    }


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        System.out.println(Arrays.toString(keyBytes));
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    /*
//    public UserRoles extractRole(String token) {
//        Claims claims = extractAllClaims(token);
//        String roleStr = claims.get("role", String.class); // Extract the role as a String
//
//        try {
//            return UserRoles.valueOf(roleStr); // Convert the String to UserRoles enum
//        } catch (IllegalArgumentException e) {
//            // Handle the case where the role is not valid
//            return null; // Or throw an exception if you prefer
//        }
//    }
*/
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userIdObj = claims.get("userId");

        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        } else {
            return null;
        }
    }

    public boolean isAdmin(String token) {
        long id = extractUserId(token);
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> value.getUserRoles().equals(UserRoles.ROLE_ADMIN)).orElse(false);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
