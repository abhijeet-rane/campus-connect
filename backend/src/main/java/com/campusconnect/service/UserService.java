package com.campusconnect.service;

import com.campusconnect.dto.request.UserRegistrationRequest;
import com.campusconnect.dto.request.UserUpdateRequest;
import com.campusconnect.dto.response.UserResponse;
import com.campusconnect.entity.User;
import com.campusconnect.entity.UserRole;
import com.campusconnect.exception.BadRequestException;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for User entity operations
 * Handles business logic for user management
 * 
 * @author Campus Connect Team
 */
@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        logger.info("UserService initialized successfully");
    }

    /**
     * Create a new user
     * @param registrationRequest the user registration request
     * @return the created user response
     */
    public UserResponse createUser(UserRegistrationRequest registrationRequest) {
        logger.info("Creating new user with email: {}", registrationRequest.getEmail());
        
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                logger.warn("Attempt to create user with existing email: {}", registrationRequest.getEmail());
                throw new BadRequestException("User with this email already exists");
            }
            
            if (userRepository.existsByUsername(registrationRequest.getUsername())) {
                logger.warn("Attempt to create user with existing username: {}", registrationRequest.getUsername());
                throw new BadRequestException("User with this username already exists");
            }

            // Create new user entity
            User user = new User();
            user.setUsername(registrationRequest.getUsername());
            user.setEmail(registrationRequest.getEmail());
            user.setPasswordHash(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setRole(UserRole.STUDENT); // Default role
            user.setDepartment(registrationRequest.getDepartment());
            user.setAcademicYear(registrationRequest.getAcademicYear());
            user.setIsActive(true);
            user.setEmailVerified(false);

            User savedUser = userRepository.save(user);
            logger.info("Successfully created user with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
            
            return convertToUserResponse(savedUser);
            
        } catch (Exception e) {
            logger.error("Error creating user with email: {}", registrationRequest.getEmail(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /**
     * Get user by ID
     * @param userId the user ID
     * @return the user response
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        logger.debug("Fetching user with ID: {}", userId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("User not found with ID: {}", userId);
                        return new ResourceNotFoundException("User", "id", userId);
                    });
            
            logger.debug("Successfully fetched user with ID: {}", userId);
            return convertToUserResponse(user);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching user with ID: {}", userId, e);
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    /**
     * Get user by email
     * @param email the user email
     * @return the user response
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        logger.debug("Fetching user with email: {}", email);
        
        try {
            User user = userRepository.findByEmailAndIsActive(email, true)
                    .orElseThrow(() -> {
                        logger.warn("Active user not found with email: {}", email);
                        return new ResourceNotFoundException("User", "email", email);
                    });
            
            logger.debug("Successfully fetched user with email: {}", email);
            return convertToUserResponse(user);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching user with email: {}", email, e);
            throw new RuntimeException("Failed to fetch user", e);
        }
    }

    /**
     * Update user information
     * @param userId the user ID
     * @param updateRequest the user update request
     * @return the updated user response
     */
    public UserResponse updateUser(Long userId, UserUpdateRequest updateRequest) {
        logger.info("Updating user with ID: {}", userId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("User not found for update with ID: {}", userId);
                        return new ResourceNotFoundException("User", "id", userId);
                    });

            // Update user fields
            if (updateRequest.getFirstName() != null) {
                user.setFirstName(updateRequest.getFirstName());
            }
            if (updateRequest.getLastName() != null) {
                user.setLastName(updateRequest.getLastName());
            }
            if (updateRequest.getDepartment() != null) {
                user.setDepartment(updateRequest.getDepartment());
            }
            if (updateRequest.getAcademicYear() != null) {
                user.setAcademicYear(updateRequest.getAcademicYear());
            }
            if (updateRequest.getBio() != null) {
                user.setBio(updateRequest.getBio());
            }
            if (updateRequest.getLocation() != null) {
                user.setLocation(updateRequest.getLocation());
            }
            if (updateRequest.getGithubUsername() != null) {
                user.setGithubUsername(updateRequest.getGithubUsername());
            }
            if (updateRequest.getLinkedinUsername() != null) {
                user.setLinkedinUsername(updateRequest.getLinkedinUsername());
            }
            if (updateRequest.getWebsiteUrl() != null) {
                user.setWebsiteUrl(updateRequest.getWebsiteUrl());
            }

            User updatedUser = userRepository.save(user);
            logger.info("Successfully updated user with ID: {}", userId);
            
            return convertToUserResponse(updatedUser);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", userId, e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    /**
     * Delete user (soft delete)
     * @param userId the user ID
     */
    public void deleteUser(Long userId) {
        logger.info("Soft deleting user with ID: {}", userId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("User not found for deletion with ID: {}", userId);
                        return new ResourceNotFoundException("User", "id", userId);
                    });

            user.setIsActive(false);
            userRepository.save(user);
            
            logger.info("Successfully soft deleted user with ID: {}", userId);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", userId, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Get all users with pagination
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        logger.debug("Fetching all users with pagination: page={}, size={}", 
                    pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<User> users = userRepository.findByIsActive(true, pageable);
            logger.debug("Successfully fetched {} users", users.getTotalElements());
            
            return users.map(this::convertToUserResponse);
            
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    /**
     * Search users by name or email
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsers(String searchTerm, Pageable pageable) {
        logger.debug("Searching users with term: '{}', page={}, size={}", 
                    searchTerm, pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<User> users = userRepository.searchUsers(searchTerm, pageable);
            logger.debug("Found {} users matching search term: '{}'", users.getTotalElements(), searchTerm);
            
            return users.map(this::convertToUserResponse);
            
        } catch (Exception e) {
            logger.error("Error searching users with term: '{}'", searchTerm, e);
            throw new RuntimeException("Failed to search users", e);
        }
    }

    /**
     * Get users by role
     * @param role the user role
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByRole(UserRole role, Pageable pageable) {
        logger.debug("Fetching users with role: {}, page={}, size={}", 
                    role, pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<User> users = userRepository.findByRole(role, pageable);
            logger.debug("Found {} users with role: {}", users.getTotalElements(), role);
            
            return users.map(this::convertToUserResponse);
            
        } catch (Exception e) {
            logger.error("Error fetching users with role: {}", role, e);
            throw new RuntimeException("Failed to fetch users by role", e);
        }
    }

    /**
     * Get users by department
     * @param department the department name
     * @param pageable pagination information
     * @return page of user responses
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByDepartment(String department, Pageable pageable) {
        logger.debug("Fetching users in department: {}, page={}, size={}", 
                    department, pageable.getPageNumber(), pageable.getPageSize());
        
        try {
            Page<User> users = userRepository.findByDepartment(department, pageable);
            logger.debug("Found {} users in department: {}", users.getTotalElements(), department);
            
            return users.map(this::convertToUserResponse);
            
        } catch (Exception e) {
            logger.error("Error fetching users in department: {}", department, e);
            throw new RuntimeException("Failed to fetch users by department", e);
        }
    }

    /**
     * Update user's last login time
     * @param userId the user ID
     */
    public void updateLastLogin(Long userId) {
        logger.debug("Updating last login for user ID: {}", userId);
        
        try {
            userRepository.updateLastLogin(userId, LocalDateTime.now());
            logger.debug("Successfully updated last login for user ID: {}", userId);
            
        } catch (Exception e) {
            logger.error("Error updating last login for user ID: {}", userId, e);
            // Don't throw exception for login time update failure
        }
    }

    /**
     * Get user statistics
     * @return user statistics
     */
    @Transactional(readOnly = true)
    public UserStatistics getUserStatistics() {
        logger.debug("Fetching user statistics");
        
        try {
            long totalUsers = userRepository.countByIsActive(true);
            long totalStudents = userRepository.countByRole(UserRole.STUDENT);
            long totalAdmins = userRepository.countByRole(UserRole.ADMIN);
            
            UserStatistics stats = new UserStatistics(totalUsers, totalStudents, totalAdmins);
            logger.debug("Successfully fetched user statistics: {}", stats);
            
            return stats;
            
        } catch (Exception e) {
            logger.error("Error fetching user statistics", e);
            throw new RuntimeException("Failed to fetch user statistics", e);
        }
    }

    /**
     * Get all departments
     * @return list of department names
     */
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        logger.debug("Fetching all departments");
        
        try {
            List<String> departments = userRepository.findAllDepartments();
            logger.debug("Found {} departments", departments.size());
            
            return departments;
            
        } catch (Exception e) {
            logger.error("Error fetching departments", e);
            throw new RuntimeException("Failed to fetch departments", e);
        }
    }

    /**
     * Convert User entity to UserResponse DTO
     * @param user the user entity
     * @return the user response DTO
     */
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setDepartment(user.getDepartment());
        response.setAcademicYear(user.getAcademicYear());
        response.setBio(user.getBio());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setGithubUsername(user.getGithubUsername());
        response.setLinkedinUsername(user.getLinkedinUsername());
        response.setWebsiteUrl(user.getWebsiteUrl());
        response.setLocation(user.getLocation());
        response.setEmailVerified(user.getEmailVerified());
        response.setCreatedAt(user.getCreatedAt());
        response.setLastLogin(user.getLastLogin());
        
        return response;
    }

    /**
     * User statistics inner class
     */
    public static class UserStatistics {
        private final long totalUsers;
        private final long totalStudents;
        private final long totalAdmins;

        public UserStatistics(long totalUsers, long totalStudents, long totalAdmins) {
            this.totalUsers = totalUsers;
            this.totalStudents = totalStudents;
            this.totalAdmins = totalAdmins;
        }

        public long getTotalUsers() { return totalUsers; }
        public long getTotalStudents() { return totalStudents; }
        public long getTotalAdmins() { return totalAdmins; }

        @Override
        public String toString() {
            return String.format("UserStatistics{totalUsers=%d, totalStudents=%d, totalAdmins=%d}", 
                               totalUsers, totalStudents, totalAdmins);
        }
    }
}