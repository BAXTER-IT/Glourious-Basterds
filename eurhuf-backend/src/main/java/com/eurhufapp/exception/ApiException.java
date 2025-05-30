package com.eurhufapp.exception;

/**
 * Exception thrown when there is an error with the API operations.
 * This is used for both internal API errors and external API calls (like Yahoo Finance).
 */
public class ApiException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new API exception with the specified detail message.
     * 
     * @param message The detail message
     */
    public ApiException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new API exception with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}