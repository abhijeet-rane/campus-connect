package com.campusconnect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Campus Connect Backend
 * 
 * @author Campus Connect Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class CampusConnectApplication {

    private static final Logger logger = LoggerFactory.getLogger(CampusConnectApplication.class);

    public static void main(String[] args) {
        logger.info("Starting Campus Connect Backend Application...");
        
        try {
            SpringApplication.run(CampusConnectApplication.class, args);
            logger.info("Campus Connect Backend Application started successfully!");
        } catch (Exception e) {
            logger.error("Failed to start Campus Connect Backend Application", e);
            System.exit(1);
        }
    }
}