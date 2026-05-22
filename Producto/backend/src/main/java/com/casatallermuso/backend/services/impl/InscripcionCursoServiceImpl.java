package com.casatallermuso.backend.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        Long cuposRestantes = curso.getCupos() - inscripcionRepository.countByCurso(curso);
        try {
            if (cuposRestantes > 0) {
                InscripcionCurso nuevaInscripcion = new InscripcionCurso(null, usuario, curso);
                inscripcionRepository.save(nuevaInscripcion);
                return true;
            }
            return false;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public void eliminarInscripcion(Usuario usuario, Curso curso) {
        var inscripcion = inscripcionRepository.findByUsuarioAndCurso(usuario, curso)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        inscripcionRepository.delete(inscripcion);
    }

    @Override
    public boolean isUsuarioInscrito(Usuario usuario, Curso curso) {
        var inscripcion = inscripcionRepository.findByUsuarioAndCurso(usuario, curso);
        return (inscripcion.isPresent());
    }

    @Override
    public Long getCuposRestantes(Curso curso) {
        Long cuposTomados = inscripcionRepository.countByCurso(curso);
        Long cuposRestantes = curso.getCupos() - cuposTomados;
        return cuposRestantes;
    }

}
