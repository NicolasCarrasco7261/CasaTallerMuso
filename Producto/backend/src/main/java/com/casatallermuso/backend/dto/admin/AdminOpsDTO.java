package com.casatallermuso.backend.dto.admin;

import com.casatallermuso.backend.dto.auth.AuthDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.enums.TipoRolUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class AdminOpsDTO {

    @Data
    public static class Usuarios {

        private Boolean invalidarClave;
        private Boolean activar;
        private TipoRolUsuario tipoRolUsuario;

    }

    @Data
    public static class NuevoUsuario {

        @NotNull
        private AuthDTO.Login credenciales;

        @NotNull
        private UsuarioDTO.Cuenta cuenta;

    }

}
