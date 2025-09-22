package com.campusconnect.exception;

/**
 * Exception thrown for business logic violations
 * 
 * @author Campus Connect Team
 */
public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}