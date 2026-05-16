package com.casatallermuso.backend.dto.usuario;

import com.casatallermuso.backend.enums.TipoRolUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolDTO {

    @NotNull
    TipoRolUsuario tipoRol;
    
}
