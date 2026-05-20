package com.casatallermuso.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Curso;

public interface CursoService {

    Page<Curso> listarCursos(Pageable pageable);
    Curso obtenerPorID(UUID id);
    Curso crearCurso(Curso nuevoCurso);

}
