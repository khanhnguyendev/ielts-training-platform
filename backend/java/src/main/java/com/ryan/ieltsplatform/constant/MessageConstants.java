package com.ryan.ieltsplatform.constant;

/**
 * Centralized constants for API messages and error codes.
 * All response messages and error codes should be defined here.
 */
public final class MessageConstants {

    private MessageConstants() {
        // Prevent instantiation
    }

    // ==================== SUCCESS MESSAGES ====================
    
    public static final class Success {
        public static final String OPERATION_COMPLETED = "Operation completed successfully";
        public static final String HELLO_MESSAGE = "Hello from IELTS Platform Backend!";
        public static final String PERSONALIZED_HELLO = "Hello, %s! Welcome to IELTS Platform Backend!";
        public static final String USER_CREATED = "User created successfully";
        public static final String USER_UPDATED = "User updated successfully";
        public static final String USER_DELETED = "User deleted successfully";
        public static final String DATA_RETRIEVED = "Data retrieved successfully";
    }

    // ==================== ERROR MESSAGES ====================
    
    public static final class Error {
        public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
        public static final String VALIDATION_FAILED = "Validation failed for %d field(s)";
        public static final String NAME_EMPTY = "Name cannot be empty";
        public static final String NAME_TOO_LONG = "Name cannot exceed %d characters";
        public static final String USER_NOT_FOUND = "User not found with id: %s";
        public static final String EMAIL_ALREADY_EXISTS = "Email already exists: %s";
        public static final String INVALID_CREDENTIALS = "Invalid credentials";
        public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
        public static final String RESOURCE_NOT_FOUND = "Resource not found";
        public static final String DUPLICATE_RESOURCE = "Resource already exists";
    }

    // ==================== ERROR CODES ====================
    
    public static final class ErrorCode {
        // Validation Errors (400)
        public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
        public static final String EMPTY_NAME = "EMPTY_NAME";
        public static final String NAME_TOO_LONG = "NAME_TOO_LONG";
        public static final String INVALID_EMAIL = "INVALID_EMAIL";
        public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
        
        // Business Errors (400)
        public static final String BUSINESS_ERROR = "BUSINESS_ERROR";
        public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
        public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
        public static final String DUPLICATE_RESOURCE = "DUPLICATE_RESOURCE";
        public static final String INVALID_OPERATION = "INVALID_OPERATION";
        
        // Authentication Errors (401/403)
        public static final String UNAUTHORIZED = "UNAUTHORIZED";
        public static final String FORBIDDEN = "FORBIDDEN";
        public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
        public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
        
        // System Errors (500)
        public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        public static final String DATABASE_ERROR = "DATABASE_ERROR";
        public static final String EXTERNAL_SERVICE_ERROR = "EXTERNAL_SERVICE_ERROR";
        
        // Demo/Test Errors
        public static final String DEMO_ERROR = "DEMO_ERROR";
        public static final String DEMO_VALIDATION_ERROR = "DEMO_VALIDATION_ERROR";
    }

    // ==================== FIELD NAMES ====================
    
    public static final class Field {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String USER_ID = "userId";
        public static final String CORRELATION_ID = "correlationId";
    }

    // ==================== VALIDATION CONSTRAINTS ====================
    
    public static final class Validation {
        public static final int MAX_NAME_LENGTH = 50;
        public static final int MIN_PASSWORD_LENGTH = 8;
        public static final int MAX_PASSWORD_LENGTH = 128;
        public static final int MAX_EMAIL_LENGTH = 255;
    }
}
