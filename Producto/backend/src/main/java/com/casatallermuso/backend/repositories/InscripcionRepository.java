package com.casatallermuso.backend.repositories;

import com.casatallermuso.backend.entities.Actividad;
import com.casatallermuso.backend.entities.Inscripcion;
import com.casatallermuso.backend.entities.Usuario;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface InscripcionRepository<
    I extends Inscripcion<A>,
    A extends Actividad
> extends JpaRepository<I, UUID> {
    Page<I> findByUsuario(Usuario usuario, Pageable pageable);
    Page<I> findByActividad(A actividad, Pageable pageable);
    Optional<I> findByUsuarioAndActividad(Usuario usuario, A actividad);
    Long countByActividad(A actividad);
}
