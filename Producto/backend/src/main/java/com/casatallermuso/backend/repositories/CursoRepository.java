package com.casatallermuso.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.casatallermuso.backend.entities.Curso;

public interface CursoRepository extends JpaRepository <Curso, Long>{

}
