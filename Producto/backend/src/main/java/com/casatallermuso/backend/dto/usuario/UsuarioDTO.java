package com.casatallermuso.backend.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class UsuarioDTO {

    @Data
    public static class Perfil {
        
        @NotBlank
        private String nombre;
        
        @NotBlank
        private String apellido;

        @NotNull
        private DetalleDTO detalle;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PerfilCorreo extends Perfil {

        @NotBlank
        @Email
        private String correo;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Cuenta extends PerfilCorreo {

        @NotNull
        private RolDTO rol;

        @NotNull
        private Boolean activo;

    }

}
