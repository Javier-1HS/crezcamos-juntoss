package com.cloudnative.academy.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Proveedor de JWT para crear y validar tokens de autenticación
 */
@Component
public class JwtTokenProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${app.jwt.secret:tu-secreto-super-seguro-debe-ser-largo-min-32-caracteres-cambiar-en-prod}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}")  // 24 horas por defecto
    private long jwtExpirationMs;
    
    /**
     * Genera un JWT token para un usuario
     * @param username El nombre de usuario
     * @return Token JWT firmado
     */
    public String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .sign(algorithm);
        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage());
            throw new RuntimeException("Error al generar token JWT", e);
        }
    }
    
    /**
     * Obtiene el username del token JWT
     * @param token El token JWT
     * @return El username extraído del token
     */
    public String getUsernameFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decoded = verifier.verify(token);
            return decoded.getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Error extracting username from JWT: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Valida un JWT token
     * @param token El token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.warn("JWT validation error: {}", e.getMessage());
            return false;
        }
    }
}
