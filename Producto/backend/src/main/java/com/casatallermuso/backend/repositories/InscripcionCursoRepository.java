package com.casatallermuso.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;

@Repository
public interface InscripcionCursoRepository extends JpaRepository<InscripcionCurso, UUID> {

    Page<InscripcionCurso> findByUsuario(Usuario usuario, Pageable pageable);
    Page<InscripcionCurso> findByCurso(Curso curso, Pageable pageable);
    Optional<InscripcionCurso> findByUsuarioAndCurso(Usuario usuario, Curso curso);
    Long countByCurso(Curso curso);
    
}
