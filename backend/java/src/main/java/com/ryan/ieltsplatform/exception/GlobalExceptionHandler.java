package com.ryan.ieltsplatform.exception;

import com.ryan.ieltsplatform.dto.ApiResponse;
import com.ryan.ieltsplatform.util.CorrelationIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST API endpoints.
 * Provides consistent error responses across all endpoints.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle validation exceptions from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        String errorMessage = "Validation failed for " + fieldErrors.size() + " field(s)";
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("fieldErrors", fieldErrors);
        errorDetails.put("correlationId", CorrelationIdUtil.getCurrentCorrelationId());

        log.warn("Validation error: {} - Fields: {} - CorrelationId: {}", 
                errorMessage, fieldErrors.keySet(), CorrelationIdUtil.getCurrentCorrelationId());

        ApiResponse<Map<String, Object>> response = ApiResponse.error(errorMessage, "VALIDATION_ERROR");
        response.setData(errorDetails);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle business logic exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        log.warn("Business error: {} - ErrorCode: {} - CorrelationId: {}", 
                ex.getMessage(), ex.getErrorCode(), CorrelationIdUtil.getCurrentCorrelationId());

        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle validation exceptions.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationException(
            ValidationException ex, WebRequest request) {
        
        Map<String, Object> errorDetails = new HashMap<>();
        if (ex.getFieldErrors() != null) {
            errorDetails.put("fieldErrors", ex.getFieldErrors());
        }
        errorDetails.put("correlationId", CorrelationIdUtil.getCurrentCorrelationId());

        log.warn("Validation error: {} - ErrorCode: {} - CorrelationId: {}", 
                ex.getMessage(), ex.getErrorCode(), CorrelationIdUtil.getCurrentCorrelationId());

        ApiResponse<Map<String, Object>> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        response.setData(errorDetails);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle general runtime exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        log.error("Runtime error: {} - CorrelationId: {}", 
                ex.getMessage(), CorrelationIdUtil.getCurrentCorrelationId(), ex);

        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Generic error: {} - CorrelationId: {}", 
                ex.getMessage(), CorrelationIdUtil.getCurrentCorrelationId(), ex);

        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
