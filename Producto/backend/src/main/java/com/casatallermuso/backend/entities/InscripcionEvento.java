package com.casatallermuso.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "inscripciones_eventos")
public class InscripcionEvento extends Inscripcion<Evento> {

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento actividad;

}
