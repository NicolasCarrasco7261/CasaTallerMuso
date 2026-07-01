package com.casatallermuso.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.casatallermuso.backend.dto.admin.AdminOpsDTO.Usuarios;
import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.repositories.RolRepository;
import com.casatallermuso.backend.repositories.UsuarioRepository;
import com.casatallermuso.backend.services.impl.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioServiceImpl(usuarioRepository, rolRepository);
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

    @Test
    @DisplayName("obtenerPorId: debe lanzar excepcion cuando no existe")
    void obtenerPorId_notFound() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> usuarioService.obtenerPorId(id)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("obtenerPorCorreo: debe retornar el usuario cuando existe")
    void obtenerPorCorreo_ok() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("ana@example.com");
        when(usuarioRepository.findByCorreo("ana@example.com"))
            .thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.obtenerPorCorreo("ana@example.com");

        assertEquals("ana@example.com", result.getCorreo());
        verify(usuarioRepository).findByCorreo("ana@example.com");
    }

    @Test
    @DisplayName("eliminarUsuario: debe eliminar cuando existe")
    void eliminarUsuario_ok() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.existsById(id)).thenReturn(true);

        usuarioService.eliminarUsuario(id);

        verify(usuarioRepository).deleteById(id);
    }

    @Test
    @DisplayName("eliminarUsuario: debe lanzar excepcion cuando no existe")
    void eliminarUsuario_notFound() {
        UUID id = UUID.randomUUID();
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.eliminarUsuario(id));

        verify(usuarioRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("saveUsuario: debe persistir el usuario recibido")
    void saveUsuario_ok() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.saveUsuario(usuario);

        assertEquals("Ana", result.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("operarSobreUsuario: debe activar o desactivar y guardar")
    void operarSobreUsuario_activar_ok() {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);
        Usuarios operaciones = new Usuarios();
        operaciones.setActivar(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.operarSobreUsuario(usuario, operaciones);

        assertThat(result.getActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("operarSobreUsuario: debe cambiar rol y guardar")
    void operarSobreUsuario_cambiarRol_ok() {
        Usuario usuario = new Usuario();
        Usuarios operaciones = new Usuarios();
        operaciones.setTipoRolUsuario(TipoRolUsuario.ADMIN);
        RolUsuario rol = new RolUsuario();
        rol.setTipoRol(TipoRolUsuario.ADMIN);

        when(rolRepository.findByTipoRol(TipoRolUsuario.ADMIN))
            .thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = usuarioService.operarSobreUsuario(usuario, operaciones);

        assertEquals(rol, result.getRol());
        verify(rolRepository).findByTipoRol(TipoRolUsuario.ADMIN);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("operarSobreUsuario: debe lanzar excepcion si el rol no existe")
    void operarSobreUsuario_rolNotFound() {
        Usuario usuario = new Usuario();
        Usuarios operaciones = new Usuarios();
        operaciones.setTipoRolUsuario(TipoRolUsuario.ADMIN);
        when(rolRepository.findByTipoRol(TipoRolUsuario.ADMIN))
            .thenReturn(Optional.empty());

        assertThrows(
            RuntimeException.class,
            () -> usuarioService.operarSobreUsuario(usuario, operaciones)
        );

        verify(usuarioRepository, never()).save(usuario);
    }

}
