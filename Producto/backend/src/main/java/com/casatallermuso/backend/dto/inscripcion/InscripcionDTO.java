package com.casatallermuso.backend.dto.inscripcion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class InscripcionDTO {
    
    @Data
    @AllArgsConstructor
    public static class UsuarioInscrito {

        @NotNull
        private Boolean inscrito;

    }
    
}
