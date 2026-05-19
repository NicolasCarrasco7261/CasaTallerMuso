package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereAuth;
import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.dto.curso.CursoDTO;
import com.casatallermuso.backend.dto.curso.CursoMapper;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.CursoService;
import com.casatallermuso.backend.services.InscripcionCursoService;
import com.casatallermuso.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoRestController {

    private final CursoService cursoService;
    private final InscripcionCursoService inscripcionService;
    private final UsuarioService usuarioService;

    private final CursoMapper cursoMapper;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    public ResponseEntity<Page<CursoDTO.CardView>> listarCursos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Curso> cursos = cursoService.listarCursos(pageable);
        Page<CursoDTO.CardView> dtoPage = cursos.map(c -> cursoMapper.toCursoCardViewDTO(c));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO.ClientView> obtenerCurso(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.obtenerPorID(id);
        CursoDTO.ClientView cursoDto = cursoMapper.toCursoClientViewDTO(curso);
        return ResponseEntity.ok(cursoDto);
    }

    @GetMapping("/{id}/a")
    public ResponseEntity<CursoDTO.AdminView> obtenerCursoAdmin(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.obtenerPorID(id);
        CursoDTO.AdminView cursoDto = cursoMapper.toCursoAdminViewDTO(curso);
        return ResponseEntity.ok(cursoDto);
    }

    @PostMapping
    public ResponseEntity<CursoDTO.AdminView> crearCurso(
        @RequestBody @Valid CursoDTO.Post cursoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Curso nuevoCurso = cursoMapper.toEntity(cursoDto);
        Curso cursoDb = cursoService.crearCurso(nuevoCurso);
        return ResponseEntity.ok(cursoMapper.toCursoAdminViewDTO(cursoDb));
    }

    // Inscripciones

    @GetMapping("/me/i")
    public ResponseEntity<Page<CursoDTO.CardView>> listarCursosInscritos(
        @RequiereAuth Claims claims,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);

        var inscripciones = inscripcionService.findByUsuario(usuario, pageable);
        var cursos = inscripciones.map((i) -> cursoMapper.toCursoCardViewDTO(i.getCurso()));
        return ResponseEntity.ok(cursos);
    }

    @PostMapping("{id}/i")
    public ResponseEntity<Void> inscribirCurso(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Curso curso = cursoService.obtenerPorID(id);

        boolean inscritoExitosamente = inscripcionService.inscribirUsuario(usuario, curso);
        if (inscritoExitosamente) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}/a/inscripciones")
    public ResponseEntity<Page<UsuarioDTO.PerfilId>> listarUsuariosInscritos(
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @PathVariable UUID id
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        Curso curso = cursoService.obtenerPorID(id);
        var inscripciones = inscripcionService.findByCurso(curso, pageable);
        var usuarios = inscripciones.map((u) -> usuarioMapper.toPerfilIdDTO(u.getUsuario()));

        return ResponseEntity.ok(usuarios);
    }
    
}
