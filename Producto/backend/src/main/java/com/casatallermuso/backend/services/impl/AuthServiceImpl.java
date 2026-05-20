package com.casatallermuso.backend.services.impl;

import java.util.Optional;
import java.util.UUID;

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
import com.casatallermuso.backend.services.AuthService;
import com.casatallermuso.backend.util.PasswordValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private PasswordValidator passwordValidator = new PasswordValidator();

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
    public Optional<String> createOrThrow(Usuario newUsuario, String clave, TipoRolUsuario tipoRolUsuario, boolean generateToken) {
        if (!passwordValidator.validate(clave)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        newUsuario.setClaveHash(
            passwordEncoder.encode(clave)
        );

        RolUsuario rolCliente = rolRepository.findByTipoRol(tipoRolUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        newUsuario.setRol(rolCliente);
        newUsuario.setActivo(true);

        Usuario usuario = usuarioRepository.save(newUsuario);

        if (generateToken) {
            return Optional.of(jwtUtils.generateToken(usuario));
        }

        return Optional.empty();
    }

    @Override
    public String updateCredentialsOrThrow(UUID usuarioId, String currentClave, String newCorreo, String newClave) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        
        if (!passwordEncoder.matches(currentClave, usuario.getClaveHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        if (newClave != null) {
            if (!passwordValidator.validate(newClave)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            usuario.setClaveHash(
                passwordEncoder.encode(newClave)
            );
        }

        if (newCorreo != null) {
            usuario.setCorreo(newCorreo);
        }

        Usuario usuarioDb = usuarioRepository.save(usuario);
        return jwtUtils.generateToken(usuarioDb);
    }
    
}
