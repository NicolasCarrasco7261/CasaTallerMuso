package com.casatallermuso.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.UsuarioRepository;
import com.casatallermuso.backend.services.UsuarioServiceImpl;

public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("listarUsuarios: debe retornar los usuarios del repositorio")
    void listarUsuarios_ok() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        var u1 = new Usuario(); u1.setId(id1); u1.setNombre("Ana");
        var u2 = new Usuario(); u2.setId(id2); u2.setNombre("Benja");
        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        var result = usuarioService.listarUsuarios();

        assertThat(result).hasSize(2);
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: debe retornar el usuario cuando existe")
    void obtenerPorId_ok() {
        UUID id = UUID.randomUUID();
        var u = new Usuario(); u.setId(id); u.setNombre("Admin");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(u));

        var result = usuarioService.obtenerPorId(id);

        assertEquals(id, result.getId());
        assertEquals("Admin", result.getNombre());
        verify(usuarioRepository).findById(id);
    }

}
