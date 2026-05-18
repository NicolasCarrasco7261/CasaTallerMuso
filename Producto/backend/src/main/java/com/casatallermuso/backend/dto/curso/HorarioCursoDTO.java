package com.casatallermuso.backend.dto.curso;

import java.time.LocalTime;

import com.casatallermuso.backend.enums.DiaSemana;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HorarioCursoDTO {

    @NotNull
    private DiaSemana diaDeSemana;

    @NotNull
    private LocalTime horaDesde;

    @NotNull
    private LocalTime horaHasta;

}
