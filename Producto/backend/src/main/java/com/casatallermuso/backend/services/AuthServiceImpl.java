package com.casatallermuso.backend.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        if (!passwordEncoder.matches(clave, usuario.getClaveHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtils.generateToken(usuario);
    }

    @Override
    public String signupOrThrow(Usuario newUsuario, String clave) {
        newUsuario.setClaveHash(
            passwordEncoder.encode(clave)
        );

        rolRepository.findByTipoRol(TipoRolUsuario.CLIENTE).ifPresent((tipoUsuario) -> {
            newUsuario.setRol(tipoUsuario);
        });
        newUsuario.setActivo(true);

        Usuario usuario = usuarioRepository.save(newUsuario);
        return jwtUtils.generateToken(usuario);
    }
    
}
