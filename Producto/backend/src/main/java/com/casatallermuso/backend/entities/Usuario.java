package com.casatallermuso.backend.entities;

import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 48)
    private String nombre;

    @Column(nullable = false, length = 48)
    private String apellido;

    @Column(nullable = false, length = 320, unique = true)
    private String correo;

    @Column(nullable = false, length = 64)
    private String claveHash;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name="tipo_usuario_id")
    private RolUsuario rol;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="detalle_usuario_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DetalleUsuario detalle;

}
