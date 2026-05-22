package com.casatallermuso.backend.dto.auth;

import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.enums.Genero;
import com.casatallermuso.backend.enums.Region;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {

        @Email
        @NotNull
        private String correo;

        @NotBlank
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
    public static class SignupFields {

        @NotNull
        private Genero[] genero;

        @NotNull
        private Region[] region;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        @NotBlank
        private String currentClave;

        @Email
        private String newCorreo;

        private String newClave;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jwt {

        private String token;

    }

}
