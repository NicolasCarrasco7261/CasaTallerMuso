package com.casatallermuso.backend.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.casatallermuso.backend.annotations.RequiereRol;
import com.casatallermuso.backend.dto.supabase.StorageDTO;
import com.casatallermuso.backend.enums.TipoBucket;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.services.S3Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilesRestController {

    private final S3Service s3Service;

    @PostMapping("img")
    public ResponseEntity<StorageDTO.KeyResponse> cargarImagen(
        @RequestParam("file") MultipartFile archivo,
        @RequiereRol(TipoRolUsuario.ADMIN) Claims claims
    ) {
        String key = s3Service.cargarArchivo(archivo, TipoBucket.IMAGENES);
        var response = new StorageDTO.KeyResponse(key);
        return ResponseEntity.ok(response);
    }

    @GetMapping("img/{key}")
    public ResponseEntity<InputStreamResource> serveImagen(@PathVariable String key) {
        var archivo = s3Service.descargarArchivo(key, TipoBucket.IMAGENES);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(archivo.response().contentType()))
            .body(new InputStreamResource(archivo));
    }

}
