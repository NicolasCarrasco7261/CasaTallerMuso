package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.services.CursoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoRestController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<Page<Curso>> listarCursos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(cursoService.listarCursos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerCurso(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.obtenerPorID(id);
        return ResponseEntity.ok(curso);
    }
    
}
