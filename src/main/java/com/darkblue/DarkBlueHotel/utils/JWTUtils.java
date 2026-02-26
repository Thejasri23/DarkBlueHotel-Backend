package com.darkblue.DarkBlueHotel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    private final SecretKey key;

    public JWTUtils() {
        String secretString = "DarkBlueHotelSuperSecureSecretKey2026!!";
        this.key = Keys.hmacShaKeyFor(
                secretString.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ✅ Generate JWT Token WITH ROLE
    public String generateToken(UserDetails userDetails) {

        String role = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority(); // example: ROLE_ADMIN

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", role) // 🔥 store role properly
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // ✅ Extract Username
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // ✅ Extract Role
    public String extractRole(String token) {
        return extractClaims(token, claims -> claims.get("role", String.class));
    }

    // ✅ Extract Any Claim
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    // ✅ Validate Token
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration)
                .before(new Date());
    }
}