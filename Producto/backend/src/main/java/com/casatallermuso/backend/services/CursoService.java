package com.casatallermuso.backend.services;

import java.util.List;

import com.casatallermuso.backend.entities.Curso;

public interface CursoService {

    Curso crear(Curso curso);
    Curso obtenerId(Long id);
    List<Curso> listarTodas();
    void eliminar(Long id);
    Curso actualizar(Long id, Curso cursoActualizado);
    Curso desactivar(Long id);

}
