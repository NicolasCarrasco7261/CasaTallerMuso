package com.casatallermuso.backend.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.repositories.CursoRepository;
import com.casatallermuso.backend.services.CursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    public Page<Curso> listarCursos(Pageable pageable) {
        return cursoRepository.findAll(pageable);
    }

    @Override
    public Curso obtenerPorID(UUID id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + id.toString()));
    }

    @Override
    public Curso crearCurso(Curso nuevoCurso) {
        return cursoRepository.save(nuevoCurso);
    }

}
