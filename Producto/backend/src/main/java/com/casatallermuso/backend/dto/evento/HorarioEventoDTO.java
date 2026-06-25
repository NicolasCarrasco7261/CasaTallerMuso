package com.casatallermuso.backend.dto.evento;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HorarioEventoDTO {

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

}
