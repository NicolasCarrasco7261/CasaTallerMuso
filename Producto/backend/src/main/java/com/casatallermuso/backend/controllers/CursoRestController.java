package com.casatallermuso.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.services.CursoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/curso")
public class CursoRestController {

    @Autowired
    private CursoService cursoServices;

    @PostMapping
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoServices.crear(curso);
        return ResponseEntity.ok(nuevoCurso);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerUsuarioPorId(@PathVariable Long id) {
        Curso curso = cursoServices.obtenerId(id);
        return ResponseEntity.ok(curso);
    }


    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        List<Curso> cursos = cursoServices.listarTodas();
        return ResponseEntity.ok(cursos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso cursoActualizado) {
        Curso curso = cursoServices.actualizar(id, cursoActualizado);
        return ResponseEntity.ok(curso);
    }


    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Curso> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(cursoServices.desactivar(id));

    }


    


}