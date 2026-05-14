package com.casatallermuso.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.CategoriaA;
import com.casatallermuso.backend.repositories.CategoriaARepository;

@Service
public class CategoriaAServiceImpl implements CategoriaAService{


    @Autowired
    private CategoriaARepository categoriaARepositories;

    @Override
    public CategoriaA crear(CategoriaA categoriaA){
        return categoriaARepositories.save(categoriaA);
    }

    @Override
    public CategoriaA obtenerId(Long id) {
        return categoriaARepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Override
    public List<CategoriaA> listarTodas() {
        return (List<CategoriaA>) categoriaARepositories.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!categoriaARepositories.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaARepositories.deleteById(id);
    }

    @Override
    public CategoriaA actualizar(Long id, CategoriaA categoriaActualizada) {
        CategoriaA existente = obtenerId(id);
        existente.setNombre(categoriaActualizada.getNombre());
        return categoriaARepositories.save(existente);
    }




}
