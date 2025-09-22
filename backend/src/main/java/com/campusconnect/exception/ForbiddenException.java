package com.campusconnect.exception;

/**
 * Exception thrown for forbidden access attempts
 * 
 * @author Campus Connect Team
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}