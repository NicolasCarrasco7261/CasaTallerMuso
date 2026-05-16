package com.casatallermuso.backend.dto.auth;

import com.casatallermuso.backend.dto.usuario.UsuarioDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        private String correo;
        private String clave;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Signup {

        @NotNull
        private Login credenciales;

        @NotNull
        private UsuarioDTO.Perfil perfil;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jwt {

        private String token;

    }

}
