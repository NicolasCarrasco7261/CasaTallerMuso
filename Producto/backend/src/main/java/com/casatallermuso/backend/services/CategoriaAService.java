package com.casatallermuso.backend.services;

import java.util.List;

import com.casatallermuso.backend.entities.CategoriaA;

public interface CategoriaAService {

    CategoriaA crear(CategoriaA categoria);
    CategoriaA obtenerId(Long id);
    List<CategoriaA> listarTodas();    
    void eliminar(Long id);
    CategoriaA actualizar(Long id, CategoriaA categoriaActualizada);
    
}
