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

    // Generate JWT Token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Extract Username
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Extract Any Claim
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(
                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
        );
    }

    // Validate Token
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration)
                .before(new Date());
    }
}


//package com.darkblue.DarkBlueHotel.utils;
//
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//import java.util.Date;
//import java.util.function.Function;
//
//@Service
//public class JWTUtils {
//
//    private static final long EXPIRATION_TIME = 1000 * 60 *24 *7; //for 7 days
//
//    private final SecretKey Key;
//
//    public JWTUtils() {
//        String secretString = "8F3A92D7K5M4Q6ZL1R9T2E7YH0CWBXGJSPV";
//        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
//        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
//    }
//    public String generateToken(UserDetails userDetails) {
//
//        return Jwts.builder()
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
//                .signWith(Key)
//                .compact();
//    }
//    //method to extract username
//    public String extractUsername(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private<T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
//        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
//    }
//    public boolean isValidToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private boolean isTokenExpired(String token) {
//       return extractClaims(token, Claims::getExpiration).before(new Date());
//    }
//}
//
