package com.casatallermuso.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.repositories.RolRepository;
import com.casatallermuso.backend.repositories.UsuarioRepository;
import com.casatallermuso.backend.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public String loginOrThrow(String correo, String clave) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        
        if (!passwordEncoder.matches(clave, usuario.getClaveHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (!usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return jwtUtils.generateToken(usuario);
    }

    @Override
    public String signupOrThrow(Usuario newUsuario, String clave) {
        newUsuario.setClaveHash(
            passwordEncoder.encode(clave)
        );

        RolUsuario rolCliente = rolRepository.findByTipoRol(TipoRolUsuario.CLIENTE)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        newUsuario.setRol(rolCliente);
        newUsuario.setActivo(true);

        Usuario usuario = usuarioRepository.save(newUsuario);
        return jwtUtils.generateToken(usuario);
    }
    
}
