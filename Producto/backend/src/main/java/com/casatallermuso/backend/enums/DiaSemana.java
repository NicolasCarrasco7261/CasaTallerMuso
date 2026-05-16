package com.casatallermuso.backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DiaSemana {

    @JsonProperty("Lunes")      LUN,
    @JsonProperty("Martes")     MAR,
    @JsonProperty("Miércoles")  MIE,
    @JsonProperty("Jueves")     JUE,
    @JsonProperty("Viernes")    VIE,
    @JsonProperty("Sábado")     SAB,
    @JsonProperty("Domingo")    DOM

}
