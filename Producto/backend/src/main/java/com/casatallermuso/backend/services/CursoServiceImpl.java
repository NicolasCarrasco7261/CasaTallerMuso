package com.casatallermuso.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.repositories.CursoRepository;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository cursoRepositories;


    @Override
    public Curso crear(Curso curso){
        return cursoRepositories.save(curso);
    }

    @Override
    public Curso obtenerId(Long id) {
        return cursoRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Override
    public List<Curso> listarTodas() {
        return (List<Curso>) cursoRepositories.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!cursoRepositories.existsById(id)) {
            throw new RuntimeException("Curso no encontrado");
        }
       cursoRepositories.deleteById(id);
    }

    @Override
    public Curso actualizar(Long id, Curso cursoActualizado) {
        Curso existente = obtenerId(id);
        existente.setTitulo(cursoActualizado.getTitulo());
        existente.setDescripcion(cursoActualizado.getDescripcion());
        existente.setPrecio(cursoActualizado.getPrecio());
        existente.setHorario(cursoActualizado.getHorario());
        existente.setCupos(cursoActualizado.getCupos());
        existente.setImg(cursoActualizado.getImg());
        existente.setCategoriaA(cursoActualizado.getCategoriaA());
        return cursoRepositories.save(existente);
    }


    @Override
    public Curso desactivar(Long id){
        Curso curso = obtenerId(id);
        curso.setActivo(false);
        return cursoRepositories.save(curso);
    }

}