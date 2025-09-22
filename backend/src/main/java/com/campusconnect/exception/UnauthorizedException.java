package com.campusconnect.exception;

/**
 * Exception thrown for unauthorized access attempts
 * 
 * @author Campus Connect Team
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}