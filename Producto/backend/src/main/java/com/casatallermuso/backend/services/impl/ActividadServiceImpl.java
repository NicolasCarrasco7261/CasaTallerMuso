package com.casatallermuso.backend.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Actividad;
import com.casatallermuso.backend.repositories.ActividadRepository;
import com.casatallermuso.backend.services.ActividadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ActividadServiceImpl<
    T extends Actividad,
    R extends ActividadRepository<T>
> implements ActividadService<T> {

    protected final R repository;
    protected final String entityName;

    @Override
    public T guardar(T nuevaEntidad) {
        return repository.save(nuevaEntidad);
    }

    @Override
    public T buscarPorID(UUID id) {
        return getEntityOrThrow(id);
    }

    @Override
    public Page<T> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<T> listarActivos(Pageable pageable) {
        return repository.findByActivo(true, pageable);
    }

    @Override
    public void eliminar(UUID id) {
        T entidad = getEntityOrThrow(id);
        repository.delete(entidad);
    }

    protected T getEntityOrThrow(UUID id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    this.entityName + "'" + id.toString() + "' not found"
                )
            );
    }
}
