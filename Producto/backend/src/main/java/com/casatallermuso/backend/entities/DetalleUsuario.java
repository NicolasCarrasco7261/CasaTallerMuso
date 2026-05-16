package com.casatallermuso.backend.entities;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.casatallermuso.backend.enums.Genero;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ubicacion_usuario_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UbicacionUsuario ubicacionUsuario;

}
