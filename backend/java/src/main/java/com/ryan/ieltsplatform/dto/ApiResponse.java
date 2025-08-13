package com.ryan.ieltsplatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryan.ieltsplatform.constant.MessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper for consistent REST API responses.
 * 
 * @param <T> The type of data being returned
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Response status (success/error)", example = "success")
    @JsonProperty("status")
    private String status;

    @Schema(description = "Response data payload")
    @JsonProperty("data")
    private T data;

    @Schema(description = "Response message", example = "Operation completed successfully")
    @JsonProperty("message")
    private String message;

    @Schema(description = "Error code (only present for errors)", example = "VALIDATION_ERROR")
    @JsonProperty("errorCode")
    private String errorCode;

    @Schema(description = "Response timestamp")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    // Constructors
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(String status, T data, String message) {
        this();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public ApiResponse(String status, String message, String errorCode) {
        this();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Static factory methods for success responses
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, MessageConstants.Success.OPERATION_COMPLETED);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("success", data, message);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>("success", null, message);
    }

    // Static factory methods for error responses
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>("error", message, errorCode);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
