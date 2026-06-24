package com.casatallermuso.backend.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.entities.Actividad;
import com.casatallermuso.backend.entities.Inscripcion;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionRepository;
import com.casatallermuso.backend.services.InscripcionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class InscripcionServiceImpl<
    I extends Inscripcion<A>,
    A extends Actividad
> implements InscripcionService<I, A> {

    protected final InscripcionRepository<I, A> repository;

    @Override
    public Optional<I> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Page<I> findByActividad(A actividad, Pageable pageable) {
        return repository.findByActividad(actividad, pageable);
    }

    @Override
    public Page<I> findByUsuario(Usuario usuario, Pageable pageable) {
        return repository.findByUsuario(usuario, pageable);
    }

    @Override
    public void inscribirUsuario(Usuario usuario, A actividad) {
        Long cuposRestantes =
            actividad.getCupos() - repository.countByActividad(actividad);

        if (cuposRestantes <= 0) {
            throw new RuntimeException(
                actividad.getClass().getSimpleName() +
                    " '" +
                    actividad.getNombre() +
                    "' no tiene cupos disponibles"
            );
        }

        I nuevaInscripcion = this.crearInscripcion(usuario, actividad);
        repository.save(nuevaInscripcion);
    }

    @Override
    public void eliminarInscripcion(Usuario usuario, A actividad) {
        I inscripcion = repository
            .findByUsuarioAndActividad(usuario, actividad)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
            );
        repository.delete(inscripcion);
    }

    @Override
    public boolean isUsuarioInscrito(Usuario usuario, A actividad) {
        var inscripcion = repository.findByUsuarioAndActividad(
            usuario,
            actividad
        );
        return inscripcion.isPresent();
    }

    @Override
    public Long getCuposRestantes(A actividad) {
        Long cuposTomados = repository.countByActividad(actividad);
        Long cuposRestantes = actividad.getCupos() - cuposTomados;
        return cuposRestantes;
    }

    protected abstract I crearInscripcion(Usuario usuario, A actividad);
}
