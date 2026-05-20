package com.casatallermuso.backend.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.entities.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


@Component
public class JwtUtils {
    
    private final SecretKey key;
    private final long expirationMillis;

    public JwtUtils(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") Duration duration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = duration.toMillis();
    }
    
    public String generateToken(Usuario usuario) {
        String sujeto = usuario.getId().toString();
        String rol = usuario.getRol().getTipoRol().name();
        Date issued = new Date();
        Date expire = new Date(issued.getTime() + expirationMillis);

        return Jwts.builder()
            .subject(sujeto)
            .claim("rol", rol)
            .issuedAt(issued)
            .expiration(expire)
            .signWith(key)
            .compact();
    }

    public Claims getPayloadOrThrow(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Token JWT expirado", e);
        } catch (SignatureException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Token JWT inválido", e);
        } catch (MalformedJwtException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Token JWT malformado", e);
        } catch (JwtException e) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Validación JWT fallida", e);
        }
    }

}
