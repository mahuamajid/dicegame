package com.example.dicegame.exception;

import com.example.dicegame.model.dto.response.base.ErrorResponse;
import com.example.dicegame.model.dto.response.base.MetaData;
import com.example.dicegame.util.DateTimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dicegame.exception.GlobalErrorStatusDictionary.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors (Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField, FieldError::getDefaultMessage
                ));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_ERROR.getMessage(),
                VALIDATION_ERROR.getStatusCode(), request.getRequestURI(), validationErrors);
    }

    // Handles BadRequestAlertException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestAlertException(BadRequestException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), VALIDATION_ERROR.getStatusCode(), request.getRequestURI(), null);
    }

    // Handles all unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                GlobalErrorStatusDictionary.INTERNAL_SERVER_ERROR.getMessage(),
                GlobalErrorStatusDictionary.INTERNAL_SERVER_ERROR.getStatusCode(),
                request.getRequestURI(),
                null
        );
    }

    // Handles ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                CONSTRAINT_VIOLATION_ERROR.getMessage(),
                CONSTRAINT_VIOLATION_ERROR.getStatusCode(),
                request.getRequestURI(),
                null
        );
    }

    // Handles DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                DATA_INTEGRITY_ERROR.getMessage(),
                DATA_INTEGRITY_ERROR.getStatusCode(),
                request.getRequestURI(),
                null
        );
    }

    // Handles PlayerException
    @ExceptionHandler(PlayerException.class)
    public ResponseEntity<ErrorResponse> handlePlayerException(PlayerException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ex.getStatusCode(),
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String errorKey, String path, Map<String, String> errors) {
        return buildErrorResponse(status, message, errorKey, path, errors, null);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String errorKey, String path, Map<String, String> errors, String entityName) {
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(message)
                        .errorKey(errorKey)
                        .path(path)
                        .entityName(entityName)
                        .errors(errors)
                        .meta(MetaData.builder().timestamp(DateTimeUtils.getIsoTimestamp()).build())
                        .build());
    }
}
