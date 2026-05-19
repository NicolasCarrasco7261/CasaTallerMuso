package com.casatallermuso.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {

}
