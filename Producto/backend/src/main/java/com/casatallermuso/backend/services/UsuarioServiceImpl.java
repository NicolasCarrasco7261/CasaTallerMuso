package com.casatallermuso.backend.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.UsuarioRepository;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    // Repositorio para acceder a la base de datos (usuarios)
    private final UsuarioRepository usuarioRepositories; 

    // Inyección por constructor del repositorio y del bean global del encoder
    public UsuarioServiceImpl(UsuarioRepository usuarioRepositories) {
        this.usuarioRepositories = usuarioRepositories;
    }

    // ===== CRUD =====

    @Override
    public Usuario crear(Usuario usuario) {
        return usuarioRepositories.save(usuario);
    }

    // Listar y obtener a todos los usuarios 
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepositories.findAll();
    }

    // Obtener un usuario por su ID
    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Actualizar un usuario existente
    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario existente = obtenerPorId(id);

        existente.setNombre(usuarioActualizado.getNombre());
        existente.setApellido(usuarioActualizado.getApellido());
        existente.setCorreo(usuarioActualizado.getCorreo());
        existente.setContrasenia(usuarioActualizado.getContrasenia());
        existente.setCategoriaU(usuarioActualizado.getCategoriaU());
        // No se actualiza el estado ni la categoría desde aquí, eso se hace con otro endpoint
        return usuarioRepositories.save(existente);
    }

    // Eliminar un usuario por su ID
    @Override
    public void eliminar(Long id) {
        if (!usuarioRepositories.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        usuarioRepositories.deleteById(id);
    }

    // Cambiar estado (ACTIVO / INACTIVO)
    @Override
    public Usuario cambiarEstado(Long id, Boolean nuevoEstado) {
        Usuario existente = obtenerPorId(id);
        existente.setActivo(nuevoEstado);
        return usuarioRepositories.save(existente);
    }

    // Obtener un usuario por su correo
    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorEmail(String correo) {
        return usuarioRepositories.findByEmail(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }
}