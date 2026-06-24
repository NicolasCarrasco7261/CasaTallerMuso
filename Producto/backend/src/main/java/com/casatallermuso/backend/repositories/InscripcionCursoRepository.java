package com.casatallermuso.backend.repositories;

import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;

@Repository
public interface InscripcionCursoRepository extends InscripcionRepository<InscripcionCurso, Curso> {
    
}
