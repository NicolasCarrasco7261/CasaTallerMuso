package com.casatallermuso.backend.dto;

import java.time.LocalDate;

import com.casatallermuso.backend.enums.Genero;
import com.casatallermuso.backend.enums.Region;
import com.casatallermuso.backend.enums.TipoRolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class UsuarioDTO {

    @Data
    public static class UbicacionUsuarioDTO {
        @NotBlank
        private String direccion;

        @NotNull
        private Region region;
    }

    @Data
    public static class DetalleUsuarioDTO {
        @Past
        private LocalDate fechaNacimiento;

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
        private String numeroTelefonico;

        private Genero genero;
        private UbicacionUsuarioDTO ubicacionUsuario;
    }

    @Data
    public static class RolDTO {
        @NotBlank
        private TipoRolUsuario nombre;
    }

    @Data
    public static class BaseDTO {
        @NotBlank
        private String nombre;

        @NotBlank
        private String apellido;

        @Email
        @NotBlank
        private String correo;
    }

    // Obtener

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Vista extends BaseDTO {
        @NotNull
        private DetalleUsuarioDTO detalleUsuario;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class VistaAdmin extends BaseDTO {
        @NotBlank
        private String tipoUsuarioNombre;

        @NotBlank
        private boolean activo;

        @NotNull
        private DetalleUsuarioDTO detalleUsuario;
    }

    // Crear

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Registro extends BaseDTO {
        @NotBlank
        private String clave;

        @NotNull
        private DetalleUsuarioDTO detalleUsuario;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Creacion extends BaseDTO {
        @NotNull
        private RolDTO rolUsuario;
    }

}
