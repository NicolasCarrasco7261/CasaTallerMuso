package com.casatallermuso.backend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TipoRolUsuario {

    @JsonProperty("Cliente")
    CLIENTE,

    @JsonProperty("Administrador")
    ADMIN
    
}
