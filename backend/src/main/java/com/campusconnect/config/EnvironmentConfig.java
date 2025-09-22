package com.campusconnect.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Configuration class for environment-specific properties
 * 
 * @author Campus Connect Team
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
public class EnvironmentConfig {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfig.class);

    private final Cors cors = new Cors();
    private final FileUpload fileUpload = new FileUpload();
    private final Pagination pagination = new Pagination();
    private final RateLimiting rateLimiting = new RateLimiting();
    private final Security security = new Security();

    public EnvironmentConfig() {
        logger.info("Initializing EnvironmentConfig");
    }

    // Getters
    public Cors getCors() {
        return cors;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public RateLimiting getRateLimiting() {
        return rateLimiting;
    }

    public Security getSecurity() {
        return security;
    }

    /**
     * CORS configuration properties
     */
    public static class Cors {
        @NotBlank
        private String allowedOrigins = "http://localhost:3000,http://localhost:5173";
        
        @NotBlank
        private String allowedMethods = "GET,POST,PUT,DELETE,OPTIONS";
        
        @NotBlank
        private String allowedHeaders = "*";
        
        @NotNull
        private Boolean allowCredentials = true;

        // Getters and Setters
        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public String getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public Boolean getAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }
    }

    /**
     * File upload configuration properties
     */
    public static class FileUpload {
        @NotBlank
        private String maxFileSize = "10MB";
        
        @NotBlank
        private String maxRequestSize = "10MB";
        
        @NotBlank
        private String uploadDir = "./uploads";

        // Getters and Setters
        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public String getMaxRequestSize() {
            return maxRequestSize;
        }

        public void setMaxRequestSize(String maxRequestSize) {
            this.maxRequestSize = maxRequestSize;
        }

        public String getUploadDir() {
            return uploadDir;
        }

        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }
    }

    /**
     * Pagination configuration properties
     */
    public static class Pagination {
        @Positive
        private Integer defaultPageSize = 20;
        
        @Positive
        private Integer maxPageSize = 100;

        // Getters and Setters
        public Integer getDefaultPageSize() {
            return defaultPageSize;
        }

        public void setDefaultPageSize(Integer defaultPageSize) {
            this.defaultPageSize = defaultPageSize;
        }

        public Integer getMaxPageSize() {
            return maxPageSize;
        }

        public void setMaxPageSize(Integer maxPageSize) {
            this.maxPageSize = maxPageSize;
        }
    }

    /**
     * Rate limiting configuration properties
     */
    public static class RateLimiting {
        @NotNull
        private Boolean enabled = true;
        
        @Positive
        private Integer requestsPerMinute = 100;

        // Getters and Setters
        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getRequestsPerMinute() {
            return requestsPerMinute;
        }

        public void setRequestsPerMinute(Integer requestsPerMinute) {
            this.requestsPerMinute = requestsPerMinute;
        }
    }

    /**
     * Security configuration properties
     */
    public static class Security {
        @Positive
        private Integer bcryptStrength = 12;
        
        @Positive
        private Integer sessionTimeout = 1800;

        // Getters and Setters
        public Integer getBcryptStrength() {
            return bcryptStrength;
        }

        public void setBcryptStrength(Integer bcryptStrength) {
            this.bcryptStrength = bcryptStrength;
        }

        public Integer getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(Integer sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }
    }
}