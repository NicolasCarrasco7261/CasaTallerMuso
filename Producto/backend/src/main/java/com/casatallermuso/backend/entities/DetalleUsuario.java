package com.casatallermuso.backend.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.casatallermuso.backend.enums.Genero;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="detalles_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 16)
    private String numeroTelefonico;

    private LocalDate fechaNacimiento;
    
    @Enumerated(EnumType.STRING)
    private Genero genero;

}
