package com.casatallermuso.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;

public interface InscripcionCursoService {
   
    public Optional<InscripcionCurso> findById(UUID id);
    public Page<InscripcionCurso> findByCurso(Curso curso, Pageable pageable);
    public Page<InscripcionCurso> findByUsuario(Usuario usuario, Pageable pageable);
    public boolean inscribirUsuario(Usuario usuario, Curso curso);
    public void eliminarInscripcion(Usuario usuario, Curso curso);

}
