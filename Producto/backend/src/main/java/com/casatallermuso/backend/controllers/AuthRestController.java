package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereAuth;
import com.casatallermuso.backend.dto.auth.AuthDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.services.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.Jwt> login(@RequestBody @Valid AuthDTO.Login loginDTO) {
        String token = authService.loginOrThrow(loginDTO.getCorreo(), loginDTO.getClave());
        return ResponseEntity.ok(new AuthDTO.Jwt(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthDTO.Jwt> signup(@RequestBody @Valid AuthDTO.Signup signupDTO) {
        // Extrae perfil y credenciales
        var perfil = signupDTO.getPerfil();
        var credenciales = signupDTO.getCredenciales();
        // Crea nuevo usuario con datos de perfil
        Usuario newUsuario = usuarioMapper.toEntity(perfil);
        newUsuario.setCorreo(credenciales.getCorreo());
        // Intenta registrar nuevo usuario
        String token = authService.signupOrThrow(newUsuario, credenciales.getClave());
        return ResponseEntity.ok(new AuthDTO.Jwt(token));
    } 

    @PutMapping("/update")
    public ResponseEntity<AuthDTO.Jwt> update(
        @RequiereAuth Claims claims,
        @RequestBody @Valid AuthDTO.Update updateDTO
    ) {
        String token = authService.updateCredentialsOrThrow(
            UUID.fromString(claims.getSubject()),
            updateDTO.getCurrentClave(),
            updateDTO.getNewCorreo(),
            updateDTO.getNewClave()
        );
        return ResponseEntity.ok(new AuthDTO.Jwt(token));
    }

}
