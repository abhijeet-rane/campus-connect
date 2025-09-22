package com.campusconnect.security;

import com.campusconnect.entity.User;
import com.campusconnect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom UserDetailsService implementation for Spring Security
 * 
 * @author Campus Connect Team
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("CustomUserDetailsService initialized");
    }

    /**
     * Load user by username (supports both email and username)
     * @param usernameOrEmail the user's username or email
     * @return UserDetails implementation
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.debug("Loading user by username or email: {}", usernameOrEmail);
        
        try {
            User user = null;
            
            // Try to find by email first
            if (usernameOrEmail.contains("@")) {
                user = userRepository.findByEmail(usernameOrEmail).orElse(null);
            }
            
            // If not found by email, try by username
            if (user == null) {
                user = userRepository.findByUsername(usernameOrEmail).orElse(null);
            }
            
            if (user == null) {
                logger.warn("User not found with username or email: {}", usernameOrEmail);
                throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
            }

            if (!user.getIsActive()) {
                logger.warn("Attempt to load inactive user: {}", email);
                throw new UsernameNotFoundException("User account is inactive: " + email);
            }

            logger.debug("Successfully loaded user: {} with role: {}", usernameOrEmail, user.getRole());
            return UserPrincipal.create(user);
            
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error loading user by email: {}", email, e);
            throw new UsernameNotFoundException("Error loading user: " + email, e);
        }
    }

    /**
     * Load user by ID (used by JWT authentication)
     * @param id the user's ID
     * @return UserDetails implementation
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        logger.debug("Loading user by ID: {}", id);
        
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User not found with ID: {}", id);
                        return new UsernameNotFoundException("User not found with ID: " + id);
                    });

            if (!user.getIsActive()) {
                logger.warn("Attempt to load inactive user with ID: {}", id);
                throw new UsernameNotFoundException("User account is inactive with ID: " + id);
            }

            logger.debug("Successfully loaded user by ID: {} with email: {}", id, user.getEmail());
            return UserPrincipal.create(user);
            
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error loading user by ID: {}", id, e);
            throw new UsernameNotFoundException("Error loading user with ID: " + id, e);
        }
    }
}