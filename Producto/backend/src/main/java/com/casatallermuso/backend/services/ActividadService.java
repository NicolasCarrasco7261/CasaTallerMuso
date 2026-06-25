package com.casatallermuso.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Actividad;

public interface ActividadService<T extends Actividad> {
    public T guardar(T nuevaEntidad);
    public T buscarPorID(UUID id);
    public Page<T> listar(Pageable pageable);
    public Page<T> listarActivos(Pageable pageable);
    public void eliminar(UUID id);
}
