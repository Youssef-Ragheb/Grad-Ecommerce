package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.UserRoles;
import com.grad.ecommerce_ai.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private final long jwtExpirationInMs; // 100h
    private final String secretKey;
    private final UserRepository userRepository;


    public JwtService(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
        this.secretKey = secretKey;
        this.jwtExpirationInMs = 1000 * 60 * 60 * 100;
        this.userRepository = userRepository;
    }


    public String generateResetToken(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 15 * 60 * 1000); // 15 minutes

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String Username, Long id, UserRoles userRoles) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", id);
        claims.put("role", userRoles.name());
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder().addClaims(claims).setSubject(Username).setIssuedAt(now).setExpiration(expiryDate).signWith(getKey(), SignatureAlgorithm.HS256).compact();

    }


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        System.out.println(Arrays.toString(keyBytes));
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }

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
