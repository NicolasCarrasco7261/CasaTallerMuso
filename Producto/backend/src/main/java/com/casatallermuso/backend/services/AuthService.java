package com.casatallermuso.backend.services;

import java.util.Optional;
import java.util.UUID;

import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;

public interface AuthService {
    
    public String loginOrThrow(String correo, String clave);
    public Optional<String> createOrThrow(Usuario newUsuario, String clave, TipoRolUsuario tipoRolUsuario, boolean generateToken);
    public String updateCredentialsOrThrow(UUID usuarioId, String currentClave, String newCorreo, String newClave);

}
