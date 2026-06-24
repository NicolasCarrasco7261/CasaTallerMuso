package com.casatallermuso.backend.services.impl;

import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.repositories.EventoRepository;

@Service
public class EventoServiceImpl
    extends ActividadServiceImpl<Evento, EventoRepository>
{

    public EventoServiceImpl(EventoRepository repository) {
        super(repository, "Evento");
    }
}
