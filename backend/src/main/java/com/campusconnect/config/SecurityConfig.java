package com.campusconnect.config;

import com.campusconnect.security.CustomUserDetailsService;
import com.campusconnect.security.JwtAuthenticationEntryPoint;
import com.campusconnect.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for Campus Connect application
 * 
 * @author Campus Connect Team
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private EnvironmentConfig environmentConfig;

    public SecurityConfig() {
        logger.info("Initializing SecurityConfig");
    }

    /**
     * JWT Authentication Filter Bean
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        logger.debug("Creating JwtAuthenticationFilter bean");
        return new JwtAuthenticationFilter();
    }

    /**
     * Password Encoder Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        int strength = environmentConfig.getSecurity().getBcryptStrength();
        logger.info("Creating BCryptPasswordEncoder with strength: {}", strength);
        return new BCryptPasswordEncoder(strength);
    }

    /**
     * Authentication Manager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        logger.debug("Creating AuthenticationManager bean");
        return authConfig.getAuthenticationManager();
    }

    /**
     * DAO Authentication Provider Bean
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.debug("Creating DaoAuthenticationProvider bean");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Security Filter Chain Configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring Security Filter Chain");
        
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Public endpoints
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/actuator/health").permitAll()
                .requestMatchers("/api/v1/swagger-ui/**", "/api/v1/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/").permitAll()
                
                // Public read-only endpoints (for landing page)
                .requestMatchers(HttpMethod.GET, "/api/v1/events/featured").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/projects/featured").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/announcements/public").permitAll()
                
                // Admin only endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/events").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/events/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/announcements").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/announcements/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/announcements/**").hasRole("ADMIN")
                
                // User management (admin or self)
                .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                
                // Authenticated endpoints
                .requestMatchers("/api/v1/users/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/me").authenticated()
                .requestMatchers("/api/v1/events/**").authenticated()
                .requestMatchers("/api/v1/projects/**").authenticated()
                .requestMatchers("/api/v1/profile/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            );

        // Add JWT filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        logger.info("Security Filter Chain configured successfully");
        return http.build();
    }

    /**
     * CORS Configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.debug("Configuring CORS");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Parse allowed origins from environment config
        String allowedOrigins = environmentConfig.getCors().getAllowedOrigins();
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        
        // Parse allowed methods
        String allowedMethods = environmentConfig.getCors().getAllowedMethods();
        List<String> methods = Arrays.asList(allowedMethods.split(","));
        configuration.setAllowedMethods(methods);
        
        // Set allowed headers
        configuration.setAllowedHeaders(List.of("*"));
        
        // Allow credentials
        configuration.setAllowCredentials(environmentConfig.getCors().getAllowCredentials());
        
        // Expose headers for JWT
        configuration.setExposedHeaders(List.of("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("CORS configured with origins: {}, methods: {}", origins, methods);
        return source;
    }
}