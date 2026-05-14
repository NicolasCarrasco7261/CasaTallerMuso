package com.casatallermuso.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String etiqueta;
    private String descripcion;
    private Long precio;
    private Long cupos;
    private Boolean activo = true;
    private String img;
    private String horario;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="categoria_id")
    private CategoriaA categoriaA;

}

