package com.casatallermuso.backend.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionCursoRepository;
import com.casatallermuso.backend.services.InscripcionCursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscripcionCursoServiceImpl implements InscripcionCursoService {

    private final InscripcionCursoRepository inscripcionRepository;

    @Override
    public Optional<InscripcionCurso> findById(UUID id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public Page<InscripcionCurso> findByCurso(Curso curso, Pageable pageable) {
        return inscripcionRepository.findByCurso(curso, pageable);
    }

    @Override
    public Page<InscripcionCurso> findByUsuario(Usuario usuario, Pageable pageable) {
        return inscripcionRepository.findByUsuario(usuario, pageable);
    }

    @Override
    public boolean inscribirUsuario(Usuario usuario, Curso curso) {
        try {
            InscripcionCurso nuevaInscripcion = new InscripcionCurso(null, usuario, curso);
            inscripcionRepository.save(nuevaInscripcion);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

}
