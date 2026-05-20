package com.casatallermuso.backend.entities;

import java.time.LocalTime;
import java.util.UUID;

import com.casatallermuso.backend.enums.DiaSemana;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "horarios_cursos")
@NoArgsConstructor
@AllArgsConstructor
public class HorarioCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaSemana diaDeSemana;

    @Column(nullable = false)
    private LocalTime horaDesde;

    @Column(nullable = false)
    private LocalTime horaHasta;

    @ManyToOne
    @JoinColumn(name="curso_id")
    @JsonIgnore
    private Curso curso;

}
