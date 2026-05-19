package com.casatallermuso.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;
import com.casatallermuso.backend.entities.Usuario;

public interface InscripcionEventoService {

    public Optional<InscripcionEvento> findById(UUID id);
    public Page<InscripcionEvento> findByEvento(Evento evento, Pageable pageable);
    public Page<InscripcionEvento> findByUsuario(Usuario usuario, Pageable pageable);
    public boolean inscribirUsuario(Usuario usuario, Evento evento);

}
