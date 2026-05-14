package com.casatallermuso.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.casatallermuso.backend.entities.CategoriaA;
import com.casatallermuso.backend.services.CategoriaAService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categoriasA")
public class CategoriaARestController {

    @Autowired
    private CategoriaAService categoriaAServices;

    @PostMapping
    public ResponseEntity<CategoriaA> crearCategoria(@RequestBody CategoriaA categoriaA) {
        CategoriaA nuevaCategoriaA = categoriaAServices.crear(categoriaA);
        return ResponseEntity.ok(nuevaCategoriaA);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoriaA> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaA categoriaA = categoriaAServices.obtenerId(id);
        return ResponseEntity.ok(categoriaA);
    }


    @GetMapping
    public ResponseEntity<List<CategoriaA>> listarCategorias() {
        List<CategoriaA> categoriasA = categoriaAServices.listarTodas();
        return ResponseEntity.ok(categoriasA);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaAServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaA> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaA categoriaActualizada) {
        CategoriaA categoriaA = categoriaAServices.actualizar(id, categoriaActualizada);
        return ResponseEntity.ok(categoriaA);
    }






}