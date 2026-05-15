package com.casatallermuso.backend.services;

import java.util.List;
import java.util.UUID;

import com.casatallermuso.backend.entities.Usuario;
public interface UsuarioService {

    Usuario crearUsuario(Usuario usuario, String claveSinCifrar);
    List<Usuario> listarUsuarios();

    Usuario obtenerPorId(UUID id);
    Usuario obtenerPorCorreo(String correo);

    void eliminarUsuario(UUID id);
    // TODO: Implementar actualizaciones parciales

}
