package com.casatallermuso.backend.services.impl;

import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionEventoRepository;

@Service
public class InscripcionEventoServiceImpl
    extends InscripcionServiceImpl<InscripcionEvento, Evento>
{

    public InscripcionEventoServiceImpl(
        InscripcionEventoRepository repository
    ) {
        super(repository);
    }

    @Override
    protected InscripcionEvento crearInscripcion(
        Usuario usuario,
        Evento actividad
    ) {
        var inscripcion = InscripcionEvento.builder()
            .usuario(usuario)
            .actividad(actividad)
            .build();
        return inscripcion;
    }
}
