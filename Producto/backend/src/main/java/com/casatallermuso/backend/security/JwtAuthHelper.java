package com.casatallermuso.backend.security;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.enums.TipoRolUsuario;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthHelper {

    private final JwtUtils jwtUtils;
    
    public Claims authenticate(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Auth Header ausente"
            );
        }
        String token = header.substring(7);
        return jwtUtils.getPayloadOrThrow(token);
    }

    public void authorizeRole(Claims claims, TipoRolUsuario requiredRole) {
        String userRole = claims.get("rol", String.class);
        if (!requiredRole.name().equals(userRole)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Rol insuficiente"
            );
        }
    }

}
