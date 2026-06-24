package com.casatallermuso.backend.repositories;

import com.casatallermuso.backend.entities.Actividad;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ActividadRepository<T extends Actividad> extends JpaRepository<T, UUID> {
    public long countByActivo(boolean activo);
}
