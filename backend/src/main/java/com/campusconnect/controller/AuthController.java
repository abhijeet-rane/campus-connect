package com.campusconnect.controller;

import com.campusconnect.dto.request.LoginRequest;
import com.campusconnect.dto.request.UserRegistrationRequest;
import com.campusconnect.dto.response.JwtAuthenticationResponse;
import com.campusconnect.dto.response.UserResponse;
import com.campusconnect.security.JwtTokenProvider;
import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.AuthService;
import com.campusconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles user authentication and registration
 * 
 * @author Campus Connect Team
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                         UserService userService,
                         JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        logger.info("AuthController initialized");
    }

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());
        
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
            
            logger.info("Successful login for user: {} with role: {}", 
                       loginRequest.getEmail(), userPrincipal.getRole());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.warn("Failed login attempt for email: {}", loginRequest.getEmail(), e);
            throw e;
        }
    }

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user account")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        logger.info("Registration attempt for email: {}", registrationRequest.getEmail());
        
        try {
            UserResponse userResponse = userService.createUser(registrationRequest);
            
            logger.info("Successful registration for user: {} with ID: {}", 
                       registrationRequest.getEmail(), userResponse.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
            
        } catch (Exception e) {
            logger.error("Failed registration attempt for email: {}", registrationRequest.getEmail(), e);
            throw e;
        }
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token", description = "Generate new JWT token using refresh token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        logger.debug("Token refresh attempt");
        
        try {
            // Remove "Bearer " prefix
            if (refreshToken.startsWith("Bearer ")) {
                refreshToken = refreshToken.substring(7);
            }
            
            if (!tokenProvider.validateToken(refreshToken) || !tokenProvider.isRefreshToken(refreshToken)) {
                logger.warn("Invalid refresh token provided");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            Long userId = tokenProvider.getUserIdFromToken(refreshToken);
            UserResponse user = userService.getUserById(userId);
            
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
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user (client should discard tokens)")
    public ResponseEntity<Void> logout() {
        logger.debug("User logout");
        
        // Clear security context
        SecurityContextHolder.clearContext();
        
        // Note: In a production system, you might want to:
        // 1. Blacklist the JWT token
        // 2. Store logout timestamp
        // 3. Invalidate refresh tokens
        
        return ResponseEntity.ok().build();
    }

    /**
     * Get current user info
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user information")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        logger.debug("Getting current user info");
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            UserResponse user = userService.getUserById(userPrincipal.getId());
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            logger.error("Error getting current user info", e);
            throw e;
        }
    }

    /**
     * Helper method to convert UserResponse to User entity
     * This is a temporary solution - in a real application, you'd have proper mapping
     */
    private com.campusconnect.entity.User convertToUserEntity(UserResponse userResponse) {
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