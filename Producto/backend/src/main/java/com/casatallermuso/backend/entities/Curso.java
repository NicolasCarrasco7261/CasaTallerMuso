package com.casatallermuso.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cursos")
public class Curso extends Actividad {

    @OneToMany(mappedBy = "curso")
    private List<HorarioCurso> horarios;
}
