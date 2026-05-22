package com.casatallermuso.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.dto.estadisticas.EstadisticasDTO;
import com.casatallermuso.backend.services.EstadisticaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class EstadisticaRestController {

    private final EstadisticaService estadisticaService;

    @GetMapping()
    public ResponseEntity<EstadisticasDTO> getEstadisticas() {
        Long cursosDisponibles = estadisticaService.getCantidadCursosDisponibles();
        Long eventosDisponibles = estadisticaService.getCantidadEventosDisponibles();
        return ResponseEntity.ok(new EstadisticasDTO(
            cursosDisponibles,
            eventosDisponibles
        ));
    }
    
}
