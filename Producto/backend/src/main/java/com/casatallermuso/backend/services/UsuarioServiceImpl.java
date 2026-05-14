package com.casatallermuso.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.UsuarioRepository;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository; 

    // Inyección por constructor del repositorio y del bean global del encoder
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ===== CRUD =====

    @Override
    public Usuario crear(Usuario usuario) {
        // Valida y encripta siempre al crear
        String raw = usuario.getPassword();
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        usuario.setPassword(usuario.getPassword());

        // Defaults por si vienen null
        if (usuario.getEstado() == null) usuario.setEstado(true);
        if (usuario.getRol() == null) usuario.setRol(Usuario.Rol.USUARIO);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario existente = obtenerPorId(id);

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setEmail(usuarioActualizado.getEmail());

        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            existente.setPassword(usuarioActualizado.getPassword());
        }

        if (usuarioActualizado.getRol() != null) {
            existente.setRol(usuarioActualizado.getRol());
        }

        // Estado se maneja en cambiarEstado(...)
        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario cambiarEstado(Long id, Boolean nuevoEstado) {
        Usuario existente = obtenerPorId(id);
        existente.setEstado(nuevoEstado = false);
        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}