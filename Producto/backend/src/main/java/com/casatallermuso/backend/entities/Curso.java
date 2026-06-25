package com.casatallermuso.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cursos")
public class Curso extends Actividad {

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioCurso> horarios = new ArrayList<>();

    public void setHorarios(List<HorarioCurso> horarios) {
        this.horarios.clear();

        if (horarios == null) {
            return;
        }

        for (HorarioCurso horario : horarios) {
            horario.setCurso(this);
            this.horarios.add(horario);
        }
    }
}
