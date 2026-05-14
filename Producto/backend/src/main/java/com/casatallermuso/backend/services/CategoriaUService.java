package com.casatallermuso.backend.services;

import java.util.List;

import com.casatallermuso.backend.entities.CategoriaU;

public interface CategoriaUService {

    CategoriaU crear(CategoriaU categoria);
    CategoriaU obtenerId(Long id);
    List<CategoriaU> listarTodas();    
    void eliminar(Long id);
    CategoriaU actualizar(Long id, CategoriaU categoriaActualizada);

    

}
