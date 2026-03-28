package com.cloudnative.academy.api;

import com.cloudnative.academy.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador de autenticación
 * Maneja login y generación de tokens JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Realiza login y retorna un token JWT
     * @param loginRequest Solicitud con credenciales
     * @return Token JWT si la autenticación es exitosa
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar credenciales (en producción, verificar contra BD)
            if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                logger.warn("Login attempt with missing credentials");
                return ResponseEntity.badRequest()
                    .body(new ErrorMessage("Usuario y contraseña son requeridos"));
            }
            
            // Aquí iría validación real contra BD
            // Por ahora, aceptamos cualquier usuario con password "password"
            if (!validateCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
                logger.warn("Failed login attempt for user: {}", loginRequest.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorMessage("Credenciales inválidas"));
            }
            
            String token = tokenProvider.generateToken(loginRequest.getUsername());
            logger.info("Successful login for user: {}", loginRequest.getUsername());
            
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Error durante la autenticación"));
        }
    }
    
    /**
     * Valida credenciales del usuario
     * NOTA: En producción, esto debería consultar la BD con hashing de contraseña
     */
    private boolean validateCredentials(String username, String password) {
        // Demo: aceptar usuario "admin" con contraseña "password123"
        // En producción: consultar BD y verificar hash de contraseña
        return "admin".equals(username) && "password123".equals(password);
    }
    
    /**
     * DTO para solicitud de login
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    /**
     * DTO para respuesta de token
     */
    public static class TokenResponse {
        private String token;
        private String type = "Bearer";
        
        public TokenResponse(String token) {
            this.token = token;
        }
        
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
    
    /**
     * DTO para mensajes de error
     */
    public static class ErrorMessage {
        private String message;
        
        public ErrorMessage(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
