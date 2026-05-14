package com.casatallermuso.backend.services;

import java.util.List;

import com.casatallermuso.backend.entities.Usuario;
public interface UsuarioService {
    // Crear un nuevo usuarios
    Usuario crear(Usuario usuario);

    // Listar todos los usuarios
    List<Usuario> listarTodos();

    // Obtener un usuario por su ID
    Usuario obtenerPorId(Long id);

    // Actualizar un usuario existente
    Usuario actualizar(Long id, Usuario usuarioActualizado);

    // Eliminar un usuario por su ID
    void eliminar(Long id);

    // Cambiar estado (ACTIVO / INACTIVO)
    Usuario cambiarEstado(Long id, Boolean nuevoEstado);

    // Obtener un usuario por su correo
    Usuario obtenerPorEmail(String correo);
}
