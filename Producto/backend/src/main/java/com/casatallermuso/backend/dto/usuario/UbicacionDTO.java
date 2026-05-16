package com.casatallermuso.backend.dto.usuario;

import com.casatallermuso.backend.enums.Region;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UbicacionDTO {
    
    @NotNull
    private Region region;

    @NotBlank
    private String direccion;

}
