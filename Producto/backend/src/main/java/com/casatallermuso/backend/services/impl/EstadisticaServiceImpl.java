package com.casatallermuso.backend.services.impl;

import org.springframework.stereotype.Service;

import com.casatallermuso.backend.repositories.CursoRepository;
import com.casatallermuso.backend.repositories.EventoRepository;
import com.casatallermuso.backend.services.EstadisticaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadisticaServiceImpl implements EstadisticaService {

    private final CursoRepository cursoRepository;
    private final EventoRepository eventoRepository;

    @Override
    public Long getCantidadCursosDisponibles() {
        return cursoRepository.countByActivo(true);
    }

    @Override
    public Long getCantidadEventosDisponibles() {
        return eventoRepository.countByActivo(true);
    }
    
}
