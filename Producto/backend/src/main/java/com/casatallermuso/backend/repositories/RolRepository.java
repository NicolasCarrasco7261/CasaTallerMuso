package com.casatallermuso.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;

@Repository
public interface RolRepository extends JpaRepository<RolUsuario, UUID> {

    Optional<RolUsuario> findByTipoRol(TipoRolUsuario tipoRol);
    boolean existsByTipoRol(TipoRolUsuario tipoRol);
    
}
