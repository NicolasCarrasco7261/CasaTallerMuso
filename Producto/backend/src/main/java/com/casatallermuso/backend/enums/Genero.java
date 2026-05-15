package com.casatallermuso.backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Genero {

    @JsonProperty("Masculino")
    MASCULINO,

    @JsonProperty("Femenino")
    FEMENINO,

    @JsonProperty("No binario")
    NO_BINARIO,

    @JsonProperty("Prefiero no responder")
    NO_ESPECIFICADO
    
}
