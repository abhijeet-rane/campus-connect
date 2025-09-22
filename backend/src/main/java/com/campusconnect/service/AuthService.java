package com.campusconnect.service;

import com.campusconnect.dto.request.LoginRequest;
import com.campusconnect.dto.response.JwtAuthenticationResponse;
import com.campusconnect.security.JwtTokenProvider;
import com.campusconnect.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for authentication operations
 * 
 * @author Campus Connect Team
 */
@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                      JwtTokenProvider tokenProvider,
                      UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        logger.info("AuthService initialized");
    }

    /**
     * Authenticate user and generate JWT tokens
     * @param loginRequest the login request
     * @return JWT authentication response
     */
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Authenticating user: {}", loginRequest.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String jwt = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            // Update last login time
            userService.updateLastLogin(userPrincipal.getId());
            
            JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                jwt,
                refreshToken,
                "Bearer",
                tokenProvider.getExpirationTime(),
                userPrincipal.getId(),
                userPrincipal.getEmail(),
                userPrincipal.getRole().name()
            );
            
            logger.info("Successfully authenticated user: {} with role: {}", 
                       loginRequest.getEmail(), userPrincipal.getRole());
            
            return response;
            
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            throw e;
        }
    }

    /**
     * Refresh JWT token using refresh token
     * @param refreshToken the refresh token
     * @return new JWT authentication response
     */
    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        logger.debug("Refreshing JWT token");
        
        try {
            if (!tokenProvider.validateToken(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
                logger.warn("Invalid refresh token provided");
                throw new RuntimeException("Invalid refresh token");
            }
            
            Long userId = tokenProvider.getUserIdFromToken(refreshToken);
            var user = userService.getUserById(userId);
            
            // Create new authentication for token generation
            UserPrincipal userPrincipal = UserPrincipal.create(convertToUserEntity(user));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
            
            String newJwt = tokenProvider.generateToken(authentication);
            String newRefreshToken = tokenProvider.generateRefreshToken(authentication);
            
            JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                newJwt,
                newRefreshToken,
                "Bearer",
                tokenProvider.getExpirationTime(),
                user.getId(),
                user.getEmail(),
                user.getRole().name()
            );
            
            logger.debug("Successfully refreshed token for user ID: {}", userId);
            return response;
            
        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            throw new RuntimeException("Failed to refresh token", e);
        }
    }

    /**
     * Logout user (clear security context)
     */
    public void logout() {
        logger.debug("User logout");
        SecurityContextHolder.clearContext();
        
        // In a production system, you might want to:
        // 1. Blacklist the JWT token
        // 2. Store logout timestamp
        // 3. Invalidate refresh tokens in database
    }

    /**
     * Validate JWT token
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        logger.debug("Validating JWT token");
        
        try {
            return tokenProvider.validateToken(token);
        } catch (Exception e) {
            logger.error("Error validating token", e);
            return false;
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
            return tokenProvider.getUserIdFromToken(token);
        } catch (Exception e) {
            logger.error("Error extracting user ID from token", e);
            throw new RuntimeException("Failed to extract user ID from token", e);
        }
    }

    /**
     * Helper method to convert UserResponse to User entity
     * This is a temporary solution - in a real application, you'd have proper mapping
     */
    private com.campusconnect.entity.User convertToUserEntity(com.campusconnect.dto.response.UserResponse userResponse) {
        com.campusconnect.entity.User user = new com.campusconnect.entity.User();
        user.setId(userResponse.getId());
        user.setEmail(userResponse.getEmail());
        user.setFirstName(userResponse.getFirstName());
        user.setLastName(userResponse.getLastName());
        user.setRole(userResponse.getRole());
        user.setDepartment(userResponse.getDepartment());
        user.setAcademicYear(userResponse.getAcademicYear());
        user.setBio(userResponse.getBio());
        user.setAvatarUrl(userResponse.getAvatarUrl());
        user.setGithubUsername(userResponse.getGithubUsername());
        user.setLinkedinUsername(userResponse.getLinkedinUsername());
        user.setWebsiteUrl(userResponse.getWebsiteUrl());
        user.setLocation(userResponse.getLocation());
        user.setEmailVerified(userResponse.getEmailVerified());
        user.setIsActive(true);
        return user;
    }
}