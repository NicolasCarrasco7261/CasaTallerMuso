package com.casatallermuso.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Actividad;
import com.casatallermuso.backend.entities.Inscripcion;
import com.casatallermuso.backend.entities.Usuario;

public interface InscripcionService<
    I extends Inscripcion<A>,
    A extends Actividad
> {
    public Optional<I> findById(UUID id);
    public Page<I> findByActividad(A actividad, Pageable pageable);
    public Page<I> findByUsuario(Usuario usuario, Pageable pageable);
    public boolean isUsuarioInscrito(Usuario usuario, A actividad);
    public void inscribirUsuario(Usuario usuario, A actividad);
    public void eliminarInscripcion(Usuario usuario, A actividad);
    public Long getCuposRestantes(A actividad);
}
