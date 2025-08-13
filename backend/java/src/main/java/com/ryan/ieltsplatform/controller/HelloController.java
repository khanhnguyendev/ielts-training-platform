package com.ryan.ieltsplatform.controller;

import com.ryan.ieltsplatform.dto.ApiResponse;
import com.ryan.ieltsplatform.exception.BusinessException;
import com.ryan.ieltsplatform.exception.ValidationException;
import com.ryan.ieltsplatform.util.CorrelationIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Sample REST controller to demonstrate API response structure and exception handling.
 * This controller will be removed in production and replaced with actual business endpoints.
 */
@RestController
@RequestMapping("/api/hello")
@Tag(name = "Hello", description = "Sample API endpoints for demonstration")
@Slf4j
public class HelloController {

    /**
     * Simple hello endpoint that returns a success response.
     */
    @GetMapping
    @Operation(
        summary = "Get hello message",
        description = "Returns a simple hello message with correlation ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                        "status": "success",
                        "data": {
                            "message": "Hello from IELTS Platform Backend!",
                            "correlationId": "550e8400-e29b-41d4-a716-446655440000"
                        },
                        "message": "Operation completed successfully",
                        "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<com.ryan.ieltsplatform.dto.ApiResponse<Map<String, String>>> hello() {
        String correlationId = CorrelationIdUtil.initializeCorrelationId();
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "Hello from IELTS Platform Backend!");
        data.put("correlationId", correlationId);
        
        log.info("Hello endpoint called - CorrelationId: {}", correlationId);
        
        return ResponseEntity.ok(com.ryan.ieltsplatform.dto.ApiResponse.success(data));
    }

    /**
     * Hello endpoint with name parameter that demonstrates validation.
     */
    @GetMapping("/{name}")
    @Operation(
        summary = "Get personalized hello message",
        description = "Returns a personalized hello message based on the provided name"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - invalid name",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<com.ryan.ieltsplatform.dto.ApiResponse<Map<String, String>>> helloWithName(
            @Parameter(description = "Name to greet", example = "Ryan")
            @PathVariable String name) {
        
        String correlationId = CorrelationIdUtil.initializeCorrelationId();
        
        // Validate name parameter
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be empty", "EMPTY_NAME");
        }
        
        if (name.length() > 50) {
            throw new ValidationException("Name cannot exceed 50 characters", "NAME_TOO_LONG");
        }
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "Hello, " + name + "! Welcome to IELTS Platform Backend!");
        data.put("correlationId", correlationId);
        data.put("greetedName", name);
        
        log.info("Hello endpoint called with name: {} - CorrelationId: {}", name, correlationId);
        
        return ResponseEntity.ok(com.ryan.ieltsplatform.dto.ApiResponse.success(data));
    }

    /**
     * Endpoint that demonstrates business exception handling.
     */
    @GetMapping("/error/business")
    @Operation(
        summary = "Demonstrate business exception",
        description = "This endpoint intentionally throws a business exception for testing"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Business error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<com.ryan.ieltsplatform.dto.ApiResponse<Void>> demonstrateBusinessError() {
        String correlationId = CorrelationIdUtil.initializeCorrelationId();
        
        log.warn("Business error demonstration requested - CorrelationId: {}", correlationId);
        
        throw new BusinessException("This is a demonstration of business exception handling", "DEMO_ERROR");
    }

    /**
     * Endpoint that demonstrates validation exception handling.
     */
    @GetMapping("/error/validation")
    @Operation(
        summary = "Demonstrate validation exception",
        description = "This endpoint intentionally throws a validation exception for testing"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<com.ryan.ieltsplatform.dto.ApiResponse<Void>> demonstrateValidationError() {
        String correlationId = CorrelationIdUtil.initializeCorrelationId();
        
        log.warn("Validation error demonstration requested - CorrelationId: {}", correlationId);
        
        throw new ValidationException("This is a demonstration of validation exception handling", "DEMO_VALIDATION_ERROR");
    }

    /**
     * Endpoint that demonstrates runtime exception handling.
     */
    @GetMapping("/error/runtime")
    @Operation(
        summary = "Demonstrate runtime exception",
        description = "This endpoint intentionally throws a runtime exception for testing"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ryan.ieltsplatform.dto.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<com.ryan.ieltsplatform.dto.ApiResponse<Void>> demonstrateRuntimeError() {
        String correlationId = CorrelationIdUtil.initializeCorrelationId();
        
        log.error("Runtime error demonstration requested - CorrelationId: {}", correlationId);
        
        throw new RuntimeException("This is a demonstration of runtime exception handling");
    }
}
