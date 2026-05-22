package com.casatallermuso.backend.dto.curso;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class CursoDTO {

    @Data
    private static class BaseView {

        @NotBlank
        private String nombre;

        @NotBlank
        private String imagenStorageKey;

        @NotNull
        private Integer precio;

        @NotNull
        private Integer cupos;

        private Integer cuposRestantes;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class CardView extends BaseView {

        @NotNull
        private UUID id;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ClientView extends BaseView {
        
        @NotBlank
        private String descripcion;

        @NotNull
        private List<HorarioCursoDTO> horarios;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AdminView extends BaseView {

        @NotBlank
        private String descripcion;

        @NotNull
        private List<HorarioCursoDTO> horarios;

        @NotNull
        private Boolean activo;

        @NotNull
        private LocalDateTime creadoEn;

    }

    @Data
    public static class Post {

        @NotBlank
        private String nombre;

        @NotBlank
        private String descripcion;

        @NotBlank
        private String imagenStorageKey;

        @NotNull
        private Integer precio;

        @NotNull
        private Integer cupos;

        @NotNull
        private List<HorarioCursoDTO> horarios;

        @NotNull
        private Boolean activo;

    }

    @Data
    public static class Put {

        private String nombre;
        private String descripcion;
        private String imagenStorageKey;
        private Integer precio;
        private Integer cupos;
        private List<HorarioCursoDTO> horarios;
        private Boolean activo;

    }
    
}