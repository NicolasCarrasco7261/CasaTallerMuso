package com.casatallermuso.backend.dto.usuario;

import java.time.LocalDate;

import com.casatallermuso.backend.enums.Genero;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DetalleDTO {

    @Past
    private LocalDate fechaNacimiento;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String numeroTelefonico;

    private Genero genero;
    private UbicacionDTO ubicacionUsuario;

}
