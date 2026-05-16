package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereAuth;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO.PerfilCorreo> obtenerMiUsuario(@RequiereAuth Claims claims) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        return ResponseEntity.ok(usuarioMapper.toPerfilCorreoDTO(usuario));
    }

}