package com.casatallermuso.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.dto.UsuarioDTO;
import com.casatallermuso.backend.dto.UsuarioMapper;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteRestController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDTO.ObtenerCliente> nuevoCliente(@RequestBody @Valid UsuarioDTO.CrearCliente usuarioDTO) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuarioMapper.toEntity(usuarioDTO), usuarioDTO.getClaveSinCifrar());
        return ResponseEntity.ok(usuarioMapper.toObtenerClienteDTO(nuevoUsuario));
    }

}