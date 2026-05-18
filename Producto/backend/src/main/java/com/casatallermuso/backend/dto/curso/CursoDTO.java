package com.casatallermuso.backend.dto.curso;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class CursoDTO {

    @Data
    public static class Card {

        @NotBlank
        private UUID id;

        @NotBlank
        private String nombre;

        @NotBlank
        private String imagenStorageKey;

        @NotBlank
        private Integer precio;

        @NotBlank
        private Integer cupos;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class View extends Card {

        @NotBlank
        private String descripcion;

        @NotBlank
        private List<HorarioCursoDTO> horarios;

    }
    
}