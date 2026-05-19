package com.casatallermuso.backend.services;

import java.util.List;
import java.util.UUID;

import com.casatallermuso.backend.dto.admin.AdminOpsDTO;
import com.casatallermuso.backend.entities.Usuario;


public interface UsuarioService {

    List<Usuario> listarUsuarios();
    Usuario obtenerPorId(UUID id);
    Usuario obtenerPorCorreo(String correo);

    void eliminarUsuario(UUID id);
    Usuario saveUsuario(Usuario usuario);
    Usuario operarSobreUsuario(Usuario usuario, AdminOpsDTO.Usuarios operaciones);

}
