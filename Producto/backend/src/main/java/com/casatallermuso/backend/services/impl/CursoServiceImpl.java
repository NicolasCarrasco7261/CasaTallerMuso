package com.casatallermuso.backend.services.impl;

import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.repositories.CursoRepository;

@Service
public class CursoServiceImpl
    extends ActividadServiceImpl<Curso, CursoRepository>
{

    public CursoServiceImpl(CursoRepository repository) {
        super(repository, "Curso");
    }
}
