package com.casatallermuso.backend.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionEventoRepository;
import com.casatallermuso.backend.services.InscripcionEventoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscripcionEventoServiceImpl implements InscripcionEventoService {

    private final InscripcionEventoRepository inscripcionRepository;

    @Override
    public Optional<InscripcionEvento> findById(UUID id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public Page<InscripcionEvento> findByEvento(Evento evento, Pageable pageable) {
        return inscripcionRepository.findByEvento(evento, pageable);
    }

    @Override
    public Page<InscripcionEvento> findByUsuario(Usuario usuario, Pageable pageable) {
        return inscripcionRepository.findByUsuario(usuario, pageable);
    }

    @Override
    public boolean inscribirUsuario(Usuario usuario, Evento evento) {
        try {
            InscripcionEvento nuevaInscripcion = new InscripcionEvento(null, usuario, evento);
            inscripcionRepository.save(nuevaInscripcion);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

}
