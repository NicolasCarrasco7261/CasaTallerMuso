package com.casatallermuso.backend.repositories;

import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;

@Repository
public interface InscripcionEventoRepository extends InscripcionRepository<InscripcionEvento, Evento> {
    
}
