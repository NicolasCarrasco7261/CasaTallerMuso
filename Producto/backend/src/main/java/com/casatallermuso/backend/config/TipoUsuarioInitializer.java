package com.casatallermuso.backend.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.repositories.RolRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TipoUsuarioInitializer {

    private final RolRepository tipoUsuarioRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultTiposUsuario() {
        for (TipoRolUsuario nombre : TipoRolUsuario.values()) {
            tipoUsuarioRepository.findByTipoRol(nombre)
                .orElseGet(() -> tipoUsuarioRepository.save(
                    new RolUsuario(null, nombre, null)
                ));
        }
    }
    
}
