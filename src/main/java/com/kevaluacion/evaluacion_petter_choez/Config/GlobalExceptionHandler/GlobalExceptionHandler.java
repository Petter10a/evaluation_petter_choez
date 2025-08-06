package com.kevaluacion.evaluacion_petter_choez.Config.GlobalExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kevaluacion.evaluacion_petter_choez.Exception.DuplicateResourceException;
import com.kevaluacion.evaluacion_petter_choez.Exception.ResourceNotFoundException;
import com.kevaluacion.evaluacion_petter_choez.Util.ApiGenericResponse;
import com.kevaluacion.evaluacion_petter_choez.Util.ResponseUtil;

/**
 * Global exception handler for the application.
 * Handles exceptions and returns appropriate responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        return ResponseUtil.error(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        return ResponseUtil.error(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiGenericResponse<Object> response = ApiGenericResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status("error")
                .message("Validation failed")
                .data(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}