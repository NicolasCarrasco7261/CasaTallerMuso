package com.casatallermuso.backend.dto.supabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StorageDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyResponse {
        private String key;
    }

}
