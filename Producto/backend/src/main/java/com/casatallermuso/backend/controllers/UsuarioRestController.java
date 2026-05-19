package com.casatallermuso.backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereAuth;
import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.dto.admin.AdminOpsDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
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

    @DeleteMapping("/me")
    public ResponseEntity<Void> eliminarMiUsuario(@RequiereAuth Claims claims) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        usuarioService.eliminarUsuario(usuarioId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO.PerfilCorreo> actualizarMiUsuario(
        @RequiereAuth Claims claims,
        @RequestBody @Valid UsuarioDTO.Update dto
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        usuarioMapper.updateUsuarioFromDTO(dto, usuario);
        Usuario usuarioDb = usuarioService.saveUsuario(usuario);
        return ResponseEntity.ok(usuarioMapper.toPerfilCorreoDTO(usuarioDb));
    }

    @PutMapping("/a/{id}")
    public ResponseEntity<Void> operarSobreUsuario(
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims,
        @RequestBody @Valid AdminOpsDTO.Usuarios operaciones
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        usuarioService.operarSobreUsuario(usuario, operaciones);
        return ResponseEntity.ok().build();
    }

}