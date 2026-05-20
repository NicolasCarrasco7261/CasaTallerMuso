package com.casatallermuso.backend.dto.evento;

import java.time.LocalDate;
import java.time.LocalTime;

import com.casatallermuso.backend.enums.DiaSemana;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HorarioEventoDTO {

    @NotNull
    private DiaSemana diaDeSemana;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

}
