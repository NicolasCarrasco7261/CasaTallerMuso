package com.casatallermuso.backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Region {
    
    @JsonProperty("Arica y Parinacota")
    ARICA_PARINACOTA,

    @JsonProperty("Tarapacá")
    TARAPACA,

    @JsonProperty("Antofagasta")
    ANTOFAGASTA,

    @JsonProperty("Atacama")
    ATACAMA,

    @JsonProperty("Coquimbo")
    COQUIMBO,

    @JsonProperty("Valparaíso")
    VALPARAISO,

    @JsonProperty("Región Metropolitana de Santiago")
    METROPOLITANA,

    @JsonProperty("Libertador General Bernardo O'Higgins")
    O_HIGGINS,

    @JsonProperty("Maule")
    MAULE,

    @JsonProperty("Ñuble")
    NUBLE,

    @JsonProperty("Biobío")
    BIOBIO,

    @JsonProperty("La Araucanía")
    ARAUCANIA,

    @JsonProperty("Los Ríos")
    LOS_RIOS,

    @JsonProperty("Los Lagos")
    LOS_LAGOS,

    @JsonProperty("Aysén del General Carlos Ibáñez del Campo")
    AYSEN,

    @JsonProperty("Magallanes y de la Antártica Chilena")
    MAGALLANES

}
