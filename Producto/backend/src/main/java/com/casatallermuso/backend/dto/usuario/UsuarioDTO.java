package com.casatallermuso.backend.dto.usuario;

import java.util.UUID;

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

        @NotNull
        private RolDTO rol;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PerfilId extends Perfil {

        @NotNull
        private UUID id;

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
    public static class Cuenta extends Perfil {

        @NotNull
        private Boolean activo;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CuentaId extends PerfilId {

        @NotNull
        private Boolean activo;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CuentaCorreo extends PerfilCorreo {
        
        @NotNull
        private Boolean activo;

    }

    @Data
    public static class Update {

        private String nombre;
        private String apellido;
        private DetalleDTO detalle;

    }

}
