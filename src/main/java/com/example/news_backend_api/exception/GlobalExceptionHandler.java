package com.example.news_backend_api.exception;

import com.example.news_backend_api.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.UNAUTHORIZED.value(),
            "Invalid username or password"
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.FORBIDDEN.value(),
            "You do not have permission to access this resource"
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.PAYLOAD_TOO_LARGE.value(),
            "Uploaded file exceeds the maximum allowed size"
        );
        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "Request body is missing or malformed JSON"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(org.springframework.web.HttpRequestMethodNotSupportedException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            "HTTP Method '" + ex.getMethod() + "' is not supported for this endpoint"
        );
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileStorageException(FileStorageException ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
