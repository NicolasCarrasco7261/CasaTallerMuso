package com.casatallermuso.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        private String correo;
        private String clave;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Jwt {
        private String token;
    }

}
