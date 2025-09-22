package com.campusconnect.controller;

import com.campusconnect.dto.request.UserUpdateRequest;
import com.campusconnect.dto.response.UserResponse;
import com.campusconnect.entity.UserRole;
import com.campusconnect.security.UserPrincipal;
import com.campusconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller
 * Handles user management operations
 * 
 * @author Campus Connect Team
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        logger.info("UserController initialized");
    }

    /**
     * Get current user profile
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user profile")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        logger.debug("Getting current user profile");
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            UserResponse user = userService.getUserById(userPrincipal.getId());
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            logger.error("Error getting current user profile", e);
            throw e;
        }
    }

    /**
     * Update current user profile
     */
    @PutMapping("/me")
    @Operation(summary = "Update current user", description = "Update current authenticated user profile")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @Valid @RequestBody UserUpdateRequest updateRequest,
            Authentication authentication) {
        
        logger.info("Updating current user profile");
        
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            UserResponse updatedUser = userService.updateUser(userPrincipal.getId(), updateRequest);
            
            logger.info("Successfully updated profile for user ID: {}", userPrincipal.getId());
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            logger.error("Error updating current user profile", e);
            throw e;
        }
    }

    /**
     * Get all users (Admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Get paginated list of all users (Admin only)")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.debug("Getting all users - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                    page, size, sortBy, sortDir);
        
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<UserResponse> users = userService.getAllUsers(pageable);
            
            logger.debug("Successfully retrieved {} users", users.getTotalElements());
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            logger.error("Error getting all users", e);
            throw e;
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get user details by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        logger.debug("Getting user by ID: {}", id);
        
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            logger.error("Error getting user by ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Update user by ID (Admin only or self)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Update user", description = "Update user by ID (Admin only or self)")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        
        logger.info("Updating user with ID: {}", id);
        
        try {
            UserResponse updatedUser = userService.updateUser(id, updateRequest);
            
            logger.info("Successfully updated user with ID: {}", id);
            return ResponseEntity.ok(updatedUser);
            
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Delete user by ID (Admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Delete user by ID (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        try {
            userService.deleteUser(id);
            
            logger.info("Successfully deleted user with ID: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Search users
     */
    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Search users by name or email")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.debug("Searching users with query: '{}', page: {}, size: {}", q, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserResponse> users = userService.searchUsers(q, pageable);
            
            logger.debug("Found {} users matching query: '{}'", users.getTotalElements(), q);
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            logger.error("Error searching users with query: '{}'", q, e);
            throw e;
        }
    }

    /**
     * Get users by role
     */
    @GetMapping("/role/{role}")
    @Operation(summary = "Get users by role", description = "Get users filtered by role")
    public ResponseEntity<Page<UserResponse>> getUsersByRole(
            @PathVariable UserRole role,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.debug("Getting users by role: {}, page: {}, size: {}", role, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserResponse> users = userService.getUsersByRole(role, pageable);
            
            logger.debug("Found {} users with role: {}", users.getTotalElements(), role);
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            logger.error("Error getting users by role: {}", role, e);
            throw e;
        }
    }

    /**
     * Get users by department
     */
    @GetMapping("/department/{department}")
    @Operation(summary = "Get users by department", description = "Get users filtered by department")
    public ResponseEntity<Page<UserResponse>> getUsersByDepartment(
            @PathVariable String department,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        logger.debug("Getting users by department: {}, page: {}, size: {}", department, page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserResponse> users = userService.getUsersByDepartment(department, pageable);
            
            logger.debug("Found {} users in department: {}", users.getTotalElements(), department);
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            logger.error("Error getting users by department: {}", department, e);
            throw e;
        }
    }

    /**
     * Get user statistics (Admin only)
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user statistics", description = "Get user statistics (Admin only)")
    public ResponseEntity<UserService.UserStatistics> getUserStatistics() {
        logger.debug("Getting user statistics");
        
        try {
            UserService.UserStatistics statistics = userService.getUserStatistics();
            
            logger.debug("Successfully retrieved user statistics: {}", statistics);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("Error getting user statistics", e);
            throw e;
        }
    }

    /**
     * Get all departments
     */
    @GetMapping("/departments")
    @Operation(summary = "Get all departments", description = "Get list of all departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        logger.debug("Getting all departments");
        
        try {
            List<String> departments = userService.getAllDepartments();
            
            logger.debug("Found {} departments", departments.size());
            return ResponseEntity.ok(departments);
            
        } catch (Exception e) {
            logger.error("Error getting all departments", e);
            throw e;
        }
    }
}