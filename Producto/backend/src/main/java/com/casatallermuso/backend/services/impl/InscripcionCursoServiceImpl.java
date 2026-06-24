package com.casatallermuso.backend.services.impl;

import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionCursoRepository;

@Service
public class InscripcionCursoServiceImpl
    extends InscripcionServiceImpl<InscripcionCurso, Curso>
{

    public InscripcionCursoServiceImpl(
        InscripcionCursoRepository repository
    ) {
        super(repository);
    }

    @Override
    protected InscripcionCurso crearInscripcion(
        Usuario usuario,
        Curso actividad
    ) {
        var inscripcion = InscripcionCurso.builder()
            .usuario(usuario)
            .actividad(actividad)
            .build();
        return inscripcion;
    }
}
