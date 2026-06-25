package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereAuth;
import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.dto.curso.CursoDTO;
import com.casatallermuso.backend.dto.curso.CursoMapper;
import com.casatallermuso.backend.dto.inscripcion.InscripcionDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.ActividadService;
import com.casatallermuso.backend.services.InscripcionService;
import com.casatallermuso.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoRestController {

    private final ActividadService<Curso> cursoService;
    private final InscripcionService<InscripcionCurso, Curso> inscripcionService;
    private final UsuarioService usuarioService;

    private final CursoMapper cursoMapper;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    public ResponseEntity<Page<CursoDTO.CardView>> listarCursos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "false") boolean hidden
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Curso> cursos = (hidden) ? cursoService.listar(pageable) : cursoService.listarActivos(pageable);
        Page<CursoDTO.CardView> dtoPage = cursos.map(c -> {
            var card = cursoMapper.toCursoCardViewDTO(c);
            card.setCuposRestantes(
                inscripcionService.getCuposRestantes(c).intValue()
            );
            return card;
        });

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO.ClientView> obtenerCurso(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.buscarPorID(id);
        CursoDTO.ClientView cursoDto = cursoMapper.toCursoClientViewDTO(curso);
        cursoDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(curso).intValue()
        );
        return ResponseEntity.ok(cursoDto);
    }

    @GetMapping("/{id}/a")
    public ResponseEntity<CursoDTO.AdminView> obtenerCursoAdmin(
        @PathVariable UUID id
    ) {
        Curso curso = cursoService.buscarPorID(id);
        CursoDTO.AdminView cursoDto = cursoMapper.toCursoAdminViewDTO(curso);
        cursoDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(curso).intValue()
        );
        return ResponseEntity.ok(cursoDto);
    }

    @PostMapping
    public ResponseEntity<CursoDTO.AdminView> crearCurso(
        @RequestBody @Valid CursoDTO.Post cursoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Curso nuevoCurso = cursoMapper.toEntity(cursoDto);
        Curso cursoDb = cursoService.guardar(nuevoCurso);
        return ResponseEntity.ok(cursoMapper.toCursoAdminViewDTO(cursoDb));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CursoDTO.AdminView> actualizarCurso(
        @PathVariable UUID id,
        @RequestBody CursoDTO.Put cursoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Curso curso = cursoService.buscarPorID(id);
        cursoMapper.updateCursoFromDTO(cursoDto, curso);
        Curso cursoDb = cursoService.guardar(curso);
        CursoDTO.AdminView cursoAdminDto = cursoMapper.toCursoAdminViewDTO(cursoDb);
        cursoAdminDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(cursoDb).intValue()
        );
        return ResponseEntity.ok(cursoAdminDto);
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
        var cursos = inscripciones.map(i ->
            cursoMapper.toCursoCardViewDTO(i.getActividad())
        );
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("{id}/i")
    public ResponseEntity<InscripcionDTO.UsuarioInscrito> verificarInscripcionCurso(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Curso curso = cursoService.buscarPorID(id);

        var inscrito = inscripcionService.isUsuarioInscrito(usuario, curso);
        var response = new InscripcionDTO.UsuarioInscrito(inscrito);

        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/i")
    public ResponseEntity<Void> inscribirCurso(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Curso curso = cursoService.buscarPorID(id);
        inscripcionService.inscribirUsuario(usuario, curso);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}/i")
    public ResponseEntity<Void> eliminarInscripcionCurso(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Curso curso = cursoService.buscarPorID(id);
        inscripcionService.eliminarInscripcion(usuario, curso);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/i/a")
    public ResponseEntity<Page<UsuarioDTO.PerfilCorreo>> listarUsuariosInscritos(
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @PathVariable UUID id
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        Curso curso = cursoService.buscarPorID(id);
        var inscripciones = inscripcionService.findByActividad(curso, pageable);
        var usuarios = inscripciones.map(u ->
            usuarioMapper.toPerfilCorreoDTO(u.getUsuario())
        );

        return ResponseEntity.ok(usuarios);
    }
}
