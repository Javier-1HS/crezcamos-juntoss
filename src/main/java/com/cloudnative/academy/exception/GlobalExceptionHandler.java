package com.cloudnative.academy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler para la aplicación
 * Maneja todas las excepciones de validación, autenticación y errores generales
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Maneja excepciones de validación (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        logger.warn("Validation error detected with fields: {}", errors.keySet());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validación fallida",
            HttpStatus.BAD_REQUEST.value(),
            errors
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Maneja excepciones de autenticación (BadCredentialsException)
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex) {
        
        logger.warn("Failed authentication attempt occurred");
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Credenciales inválidas",
            HttpStatus.UNAUTHORIZED.value(),
            null
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    /**
     * Maneja excepciones generales de autenticación
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex) {
        
        logger.warn("Authentication exception: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Error de autenticación",
            HttpStatus.UNAUTHORIZED.value(),
            null
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    /**
     * Maneja excepciones genéricas no controladas
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        
        logger.error("Unexpected error occurred: {}", ex.getClass().getSimpleName());
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Error interno del servidor",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            null
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(errorResponse);
    }
    
    /**
     * Clase interna para el formato de respuesta de errores
     */
    public static class ErrorResponse {
        private String message;
        private int status;
        private LocalDateTime timestamp;
        private Map<String, String> errors;
        
        public ErrorResponse(String message, int status, Map<String, String> errors) {
            this.message = message;
            this.status = status;
            this.errors = errors;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getMessage() { return message; }
        public int getStatus() { return status; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Map<String, String> getErrors() { return errors; }
    }
}
