package com.casatallermuso.backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DiaSemana {

    @JsonProperty("Lunes")
    LUNES,

    @JsonProperty("Martes")
    MARTES,

    @JsonProperty("Miércoles")
    MIERCOLES,

    @JsonProperty("Jueves")
    JUEVES,
    
    @JsonProperty("Viernes")
    VIERNES,
    
    @JsonProperty("Sábado")
    SABADO,
    
    @JsonProperty("Domingo")
    DOMINGO

}
