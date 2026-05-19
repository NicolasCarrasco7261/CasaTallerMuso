package com.casatallermuso.backend.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.repositories.EventoRepository;
import com.casatallermuso.backend.services.EventoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;

    @Override
    public Page<Evento> listarEventos(Pageable pageable) {
        return eventoRepository.findAll(pageable);
    }

    @Override
    public Evento obtenerPorID(UUID id) {
        return eventoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + id.toString()));
    }

    @Override
    public Evento crearEvento(Evento nuevoCurso) {
        return eventoRepository.save(nuevoCurso);
    }

}
