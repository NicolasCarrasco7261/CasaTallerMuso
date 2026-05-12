package com.casatallermuso.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Usuario;

@Repository
public interface UsuarioRepositories extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNombre(String nombre);

    boolean existsByEmail(String email);
    boolean existsByNombre(String nombre);
}