package com.casatallermuso.backend.services;

import com.casatallermuso.backend.entities.Usuario;

public interface AuthService {
    
    public String loginOrThrow(String correo, String clave);
    public String signupOrThrow(Usuario newUsuario, String clave);

}
