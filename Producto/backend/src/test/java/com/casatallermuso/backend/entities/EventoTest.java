package com.casatallermuso.backend.entities;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EventoTest {

    @Test
    @DisplayName("setHorarios: debe asignar el evento a cada horario")
    void setHorarios_wiresEvento() {
        Evento evento = new Evento();
        HorarioEvento fecha1 = new HorarioEvento();
        HorarioEvento fecha2 = new HorarioEvento();

        evento.setHorarios(List.of(fecha1, fecha2));

        assertThat(evento.getHorarios()).containsExactly(fecha1, fecha2);
        assertThat(fecha1.getEvento()).isSameAs(evento);
        assertThat(fecha2.getEvento()).isSameAs(evento);
    }

    @Test
    @DisplayName("setHorarios: debe limpiar horarios cuando recibe null")
    void setHorarios_nullClearsList() {
        Evento evento = new Evento();
        evento.setHorarios(List.of(new HorarioEvento()));

        evento.setHorarios(null);

        assertThat(evento.getHorarios()).isEmpty();
    }
}
