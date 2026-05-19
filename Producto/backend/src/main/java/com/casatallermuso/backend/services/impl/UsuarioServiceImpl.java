package com.casatallermuso.backend.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casatallermuso.backend.dto.admin.AdminOpsDTO.Usuarios;
import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.RolRepository;
import com.casatallermuso.backend.repositories.UsuarioRepository;
import com.casatallermuso.backend.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository; 
    private final RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + correo));
    }

    @Override
    public void eliminarUsuario(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario operarSobreUsuario(Usuario usuario, Usuarios operaciones) {
        if (operaciones.getActivar() != null) {
            usuario.setActivo(operaciones.getActivar());
        }

        if (operaciones.getInvalidarClave() != null) {
            // TODO implementar invalidación de contraseñas
        }

        if (operaciones.getTipoRolUsuario() != null) {
            var tipoRol = operaciones.getTipoRolUsuario();
            RolUsuario rol = rolRepository.findByTipoRol(tipoRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + tipoRol.name()));
            usuario.setRol(rol);
        }

        return usuarioRepository.save(usuario);
    }

}