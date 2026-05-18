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

import com.casatallermuso.backend.dto.curso.CursoDTO;
import com.casatallermuso.backend.dto.curso.CursoMapper;
import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.services.CursoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoRestController {

    private final CursoService cursoService;
    private final CursoMapper cursoMapper;

    @GetMapping
    public ResponseEntity<Page<CursoDTO.Card>> listarCursos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Curso> cursos = cursoService.listarCursos(pageable);
        Page<CursoDTO.Card> dtoPage = cursos.map(c -> cursoMapper.toCursoCardDTO(c));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO.Card> obtenerCurso(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.obtenerPorID(id);
        CursoDTO.Card cursoDto = cursoMapper.toCursoCardDTO(curso);
        return ResponseEntity.ok(cursoDto);
    }
    
}
