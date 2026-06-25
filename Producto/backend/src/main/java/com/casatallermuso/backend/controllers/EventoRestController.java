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
import com.casatallermuso.backend.dto.evento.EventoDTO;
import com.casatallermuso.backend.dto.evento.EventoMapper;
import com.casatallermuso.backend.dto.inscripcion.InscripcionDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioDTO;
import com.casatallermuso.backend.dto.usuario.UsuarioMapper;
import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.ActividadService;
import com.casatallermuso.backend.services.InscripcionService;
import com.casatallermuso.backend.services.UsuarioService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoRestController {

    private final ActividadService<Evento> eventoService;
    private final InscripcionService<InscripcionEvento, Evento> inscripcionService;
    private final UsuarioService usuarioService;

    private final EventoMapper eventoMapper;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    public ResponseEntity<Page<EventoDTO.CardView>> listarEventos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "false") boolean hidden
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Evento> eventos = (hidden) ? eventoService.listar(pageable) : eventoService.listarActivos(pageable);
        Page<EventoDTO.CardView> dtoPage = eventos.map(c -> {
            var card = eventoMapper.toEventoCardViewDTO(c);
            card.setCuposRestantes(
                inscripcionService.getCuposRestantes(c).intValue()
            );
            return card;
        });

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO.ClientView> obtenerEvento(
        @PathVariable UUID id
    ) {
        Evento evento = eventoService.buscarPorID(id);
        EventoDTO.ClientView eventoDto = eventoMapper.toEventoClientViewDTO(evento);
        eventoDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(evento).intValue()
        );
        return ResponseEntity.ok(eventoDto);
    }

    @GetMapping("/{id}/a")
    public ResponseEntity<EventoDTO.AdminView> obtenerEventoAdmin(
        @PathVariable UUID id
    ) {
        Evento evento = eventoService.buscarPorID(id);
        EventoDTO.AdminView eventoDto = eventoMapper.toEventoAdminViewDTO(
            evento
        );
        eventoDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(evento).intValue()
        );
        return ResponseEntity.ok(eventoDto);
    }

    @PostMapping
    public ResponseEntity<EventoDTO.AdminView> crearEvento(
        @RequestBody @Valid EventoDTO.Post eventoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Evento nuevoEvento = eventoMapper.toEntity(eventoDto);
        Evento eventoDb = eventoService.guardar(nuevoEvento);
        return ResponseEntity.ok(eventoMapper.toEventoAdminViewDTO(eventoDb));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventoDTO.AdminView> actualizarEvento(
        @PathVariable UUID id,
        @RequestBody EventoDTO.Put eventoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Evento evento = eventoService.buscarPorID(id);
        eventoMapper.updateEventoFromDTO(eventoDto, evento);
        Evento eventoDb = eventoService.guardar(evento);
        EventoDTO.AdminView eventoAdminDto = eventoMapper.toEventoAdminViewDTO(eventoDb);
        eventoAdminDto.setCuposRestantes(
            inscripcionService.getCuposRestantes(eventoDb).intValue()
        );
        return ResponseEntity.ok(eventoAdminDto);
    }

    // Inscripciones

    @GetMapping("/me/i")
    public ResponseEntity<Page<EventoDTO.CardView>> listarEventosInscritos(
        @RequiereAuth Claims claims,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);

        var inscripciones = inscripcionService.findByUsuario(usuario, pageable);
        var eventos = inscripciones.map(i ->
            eventoMapper.toEventoCardViewDTO(i.getActividad())
        );
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("{id}/i")
    public ResponseEntity<InscripcionDTO.UsuarioInscrito> verificarInscripcionCurso(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Evento evento = eventoService.buscarPorID(id);

        var inscrito = inscripcionService.isUsuarioInscrito(usuario, evento);
        var response = new InscripcionDTO.UsuarioInscrito(inscrito);

        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/i")
    public ResponseEntity<Void> inscribirEvento(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Evento evento = eventoService.buscarPorID(id);

        inscripcionService.inscribirUsuario(usuario, evento);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}/i")
    public ResponseEntity<Void> eliminarInscripcionEvento(
        @RequiereAuth Claims claims,
        @PathVariable UUID id
    ) {
        UUID usuarioId = UUID.fromString(claims.getSubject());
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        Evento curso = eventoService.buscarPorID(id);
        inscripcionService.eliminarInscripcion(usuario, curso);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/i/a")
    public ResponseEntity<Page<UsuarioDTO.PerfilId>> listarUsuariosInscritos(
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @PathVariable UUID id
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        Evento evento = eventoService.buscarPorID(id);
        var inscripciones = inscripcionService.findByActividad(
            evento,
            pageable
        );
        var usuarios = inscripciones.map(u ->
            usuarioMapper.toPerfilIdDTO(u.getUsuario())
        );

        return ResponseEntity.ok(usuarios);
    }
}
