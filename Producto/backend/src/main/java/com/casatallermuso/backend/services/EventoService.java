package com.casatallermuso.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Evento;

public interface EventoService {

    Page<Evento> listarEventos(Pageable pageable);
    Evento obtenerPorID(UUID id);
    Evento crearEvento(Evento nuevoCurso);

}
