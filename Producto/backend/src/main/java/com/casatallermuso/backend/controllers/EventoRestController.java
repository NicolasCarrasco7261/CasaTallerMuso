package com.casatallermuso.backend.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.dto.evento.EventoDTO;
import com.casatallermuso.backend.dto.evento.EventoMapper;
import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.EventoService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoRestController {

    private final EventoService eventoService;
    private final EventoMapper eventoMapper;

    @GetMapping
    public ResponseEntity<Page<EventoDTO.CardView>> listarEventos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        Sort sort = Sort.by("creadoEn").descending();
        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Evento> eventos = eventoService.listarEventos(pageable);
        Page<EventoDTO.CardView> dtoPage = eventos.map(c -> eventoMapper.toEventoCardViewDTO(c));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO.ClientView> obtenerEvento(
        @PathVariable UUID id
    ) {
        Evento evento = eventoService.obtenerPorID(id);
        EventoDTO.ClientView eventoDto = eventoMapper.toEventoClientViewDTO(evento);
        return ResponseEntity.ok(eventoDto);
    }

    @GetMapping("/{id}/a")
    public ResponseEntity<EventoDTO.AdminView> obtenerEventoAdmin(
        @PathVariable UUID id
    ) {
        Evento evento = eventoService.obtenerPorID(id);
        EventoDTO.AdminView eventoDto = eventoMapper.toEventoAdminViewDTO(evento);
        return ResponseEntity.ok(eventoDto);
    }

    @PostMapping
    public ResponseEntity<EventoDTO.AdminView> crearEvento(
        @RequestBody EventoDTO.Post eventoDto,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        Evento nuevoEvento = eventoMapper.toEntity(eventoDto);
        Evento eventoDb = eventoService.crearEvento(nuevoEvento);
        return ResponseEntity.ok(eventoMapper.toEventoAdminViewDTO(eventoDb));
    }
    
}
