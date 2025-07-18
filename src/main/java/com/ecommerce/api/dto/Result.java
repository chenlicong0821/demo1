package com.ecommerce.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Unified API Response Format
 * Provides consistent response structure for all API endpoints
 */
@Schema(description = "Unified API response wrapper")
public class Result<T> {
    
    @Schema(description = "Response code", example = "SUCCESS")
    private String code;
    
    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;
    
    @Schema(description = "Response data")
    private T data;
    
    @Schema(description = "Response timestamp", example = "2025-07-11T12:00:00")
    private LocalDateTime timestamp;
    
    // Private constructor to enforce factory methods
    private Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Create success response with data
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getDefaultMessage(), data);
    }
    
    /**
     * Create success response with custom message
     */
    public static <T> Result<T> successWithMessage(String message, T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * Create success response without data
     */
    public static <Void> Result<Void> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getDefaultMessage(), null);
    }
    
    /**
     * Create success response with custom message and no data
     */
    public static <Void> Result<Void> successWithMessage(String message) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, null);
    }
    
    /**
     * Create error response with error code enum and message
     */
    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }
    
    /**
     * Create error response with error code enum and data
     */
    public static <T> Result<T> error(ErrorCode errorCode, String message, T data) {
        return new Result<>(errorCode.getCode(), message, data);
    }
    
    /**
     * Create error response with error code enum (using default message)
     */
    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getDefaultMessage(), null);
    }
    
    /**
     * Create error response from exception message
     */
    public static <T> Result<T> error(String message) {
        ErrorCode errorCode = ErrorCode.fromMessage(message);
        return new Result<>(errorCode.getCode(), message, null);
    }
    
    // Getters
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    // Utility methods
    @JsonIgnore
    public boolean isSuccess() {
        return ErrorCode.SUCCESS.getCode().equals(code);
    }
    
    @JsonIgnore
    public boolean isError() {
        return !isSuccess();
    }
    
    @Override
    public String toString() {
        return String.format("Result{code='%s', message='%s', data=%s, timestamp=%s}", 
                           code, message, data, timestamp);
    }
} 