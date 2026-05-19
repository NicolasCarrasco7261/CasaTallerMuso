package com.casatallermuso.backend.services;

import java.util.UUID;

import com.casatallermuso.backend.entities.Usuario;

public interface AuthService {
    
    public String loginOrThrow(String correo, String clave);
    public String signupOrThrow(Usuario newUsuario, String clave);
    public String updateCredentialsOrThrow(UUID usuarioId, String currentClave, String newCorreo, String newClave);

}
