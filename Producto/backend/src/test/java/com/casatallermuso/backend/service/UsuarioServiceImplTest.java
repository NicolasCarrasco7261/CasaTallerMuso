package com.casatallermuso.backend.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
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
    private UsuarioRepository usuarioRepositories;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("listarTodos: debe retornar los usuarios del repositorio")
    void listarTodos_ok() {
        var u1 = new Usuario(); u1.setId(1L); u1.setNombre("Ana");
        var u2 = new Usuario(); u2.setId(2L); u2.setNombre("Benja");
        when(usuarioRepositories.findAll()).thenReturn(List.of(u1, u2));

        var result = usuarioService.listarTodos();

        assertThat(result).hasSize(2);
        verify(usuarioRepositories, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerPorId: debe retornar el usuario cuando existe")
    void obtenerPorId_ok() {
        var u = new Usuario(); u.setId(10L); u.setNombre("Admin");
        when(usuarioRepositories.findById(10L)).thenReturn(Optional.of(u));

        var result = usuarioService.obtenerPorId(10L);

        assertEquals(10L, result.getId());
        assertEquals("Admin", result.getNombre());
        verify(usuarioRepositories).findById(10L);
    }

    @Test
    @DisplayName("cambiarEstado: pasa de ACTIVO a INACTIVO")
    void cambiarEstado_ok() {
        var u = new Usuario();
        u.setId(7L);
        u.setActivo(true);

        when(usuarioRepositories.findById(7L)).thenReturn(Optional.of(u));
        when(usuarioRepositories.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = usuarioService.cambiarEstado(7L, false);

        assertEquals(false, result.getActivo());
        verify(usuarioRepositories).save(u);
    }
}
