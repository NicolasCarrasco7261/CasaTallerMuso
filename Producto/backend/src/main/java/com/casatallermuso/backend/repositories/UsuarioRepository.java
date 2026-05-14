package com.casatallermuso.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String correo);

    Optional<Usuario> findByNombre(String nombre);

    boolean existsByEmail(String correo);
    boolean existsByNombre(String nombre);
}