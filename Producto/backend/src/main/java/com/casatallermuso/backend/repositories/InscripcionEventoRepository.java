package com.casatallermuso.backend.repositories;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.InscripcionEvento;
import com.casatallermuso.backend.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InscripcionEventoRepository extends JpaRepository<InscripcionEvento, UUID> {

    Page<InscripcionEvento> findByUsuario(Usuario usuario, Pageable pageable);
    Page<InscripcionEvento> findByEvento(Evento evento, Pageable pageable);

}
