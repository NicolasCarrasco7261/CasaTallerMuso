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
@Table(name = "eventos")
public class Evento extends Actividad {

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioEvento> horarios = new ArrayList<>();

    public void setHorarios(List<HorarioEvento> horarios) {
        this.horarios.clear();

        if (horarios == null) {
            return;
        }

        for (HorarioEvento horario : horarios) {
            horario.setEvento(this);
            this.horarios.add(horario);
        }
    }
}
