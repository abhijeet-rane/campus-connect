package com.campusconnect.exception;

/**
 * Exception thrown for bad request scenarios
 * 
 * @author Campus Connect Team
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}