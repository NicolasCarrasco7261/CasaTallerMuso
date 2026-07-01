package com.casatallermuso.backend.entities;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CursoTest {

    @Test
    @DisplayName("setHorarios: debe asignar el curso a cada horario")
    void setHorarios_wiresCurso() {
        Curso curso = new Curso();
        HorarioCurso lunes = new HorarioCurso();
        HorarioCurso martes = new HorarioCurso();

        curso.setHorarios(List.of(lunes, martes));

        assertThat(curso.getHorarios()).containsExactly(lunes, martes);
        assertThat(lunes.getCurso()).isSameAs(curso);
        assertThat(martes.getCurso()).isSameAs(curso);
    }

    @Test
    @DisplayName("setHorarios: debe limpiar horarios cuando recibe null")
    void setHorarios_nullClearsList() {
        Curso curso = new Curso();
        curso.setHorarios(List.of(new HorarioCurso()));

        curso.setHorarios(null);

        assertThat(curso.getHorarios()).isEmpty();
    }
}
