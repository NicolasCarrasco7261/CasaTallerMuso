package com.casatallermuso.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.dto.AuthDTO;
import com.casatallermuso.backend.dto.UsuarioDTO;
import com.casatallermuso.backend.dto.UsuarioMapper;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.services.AuthService;

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
    public ResponseEntity<AuthDTO.Jwt> signup(@RequestBody @Valid UsuarioDTO.Registro signupDTO) {
        Usuario newUsuario = usuarioMapper.toEntity(signupDTO);
        String token = authService.signupOrThrow(newUsuario, signupDTO.getClave());
        return ResponseEntity.ok(new AuthDTO.Jwt(token));
    } 

}
