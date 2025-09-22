package com.campusconnect.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT Token Provider for generating and validating JWT tokens
 * 
 * @author Campus Connect Team
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpirationInMs;

    /**
     * Generate JWT token for authenticated user
     * @param authentication the authentication object
     * @return JWT token string
     */
    public String generateToken(Authentication authentication) {
        logger.debug("Generating JWT token for user: {}", authentication.getName());
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            String token = Jwts.builder()
                    .setSubject(Long.toString(userPrincipal.getId()))
                    .claim("email", userPrincipal.getEmail())
                    .claim("role", userPrincipal.getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            logger.debug("Successfully generated JWT token for user ID: {}", userPrincipal.getId());
            return token;
            
        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", authentication.getName(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    /**
     * Generate refresh token
     * @param authentication the authentication object
     * @return refresh token string
     */
    public String generateRefreshToken(Authentication authentication) {
        logger.debug("Generating refresh token for user: {}", authentication.getName());
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationInMs);

            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            String refreshToken = Jwts.builder()
                    .setSubject(Long.toString(userPrincipal.getId()))
                    .claim("type", "refresh")
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            logger.debug("Successfully generated refresh token for user ID: {}", userPrincipal.getId());
            return refreshToken;
            
        } catch (Exception e) {
            logger.error("Error generating refresh token for user: {}", authentication.getName(), e);
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }

    /**
     * Get user ID from JWT token
     * @param token the JWT token
     * @return user ID
     */
    public Long getUserIdFromToken(String token) {
        logger.debug("Extracting user ID from JWT token");
        
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());
            logger.debug("Successfully extracted user ID: {} from token", userId);
            return userId;
            
        } catch (Exception e) {
            logger.error("Error extracting user ID from token", e);
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

    /**
     * Validate JWT token
     * @param authToken the JWT token to validate
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String authToken) {
        logger.debug("Validating JWT token");
        
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(authToken);
            
            logger.debug("JWT token validation successful");
            return true;
            
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        } catch (Exception ex) {
            logger.error("JWT token validation failed: {}", ex.getMessage());
        }
        
        return false;
    }

    /**
     * Check if token is refresh token
     * @param token the JWT token
     * @return true if it's a refresh token, false otherwise
     */
    public boolean isRefreshToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            return "refresh".equals(claims.get("type"));
            
        } catch (Exception e) {
            logger.error("Error checking if token is refresh token", e);
            return false;
        }
    }

    /**
     * Get token expiration time in milliseconds
     * @return expiration time in milliseconds
     */
    public long getExpirationTime() {
        return jwtExpirationInMs;
    }

    /**
     * Get refresh token expiration time in milliseconds
     * @return refresh token expiration time in milliseconds
     */
    public long getRefreshExpirationTime() {
        return jwtRefreshExpirationInMs;
    }
}