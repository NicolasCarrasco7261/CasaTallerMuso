package com.casatallermuso.backend.service;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.enums.TipoRolUsuario;
import com.casatallermuso.backend.repositories.RolRepository;
import com.casatallermuso.backend.repositories.UsuarioRepository;
import com.casatallermuso.backend.security.JwtUtils;
import com.casatallermuso.backend.services.impl.AuthServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
            usuarioRepository,
            rolRepository,
            passwordEncoder,
            jwtUtils
        );
    }

    @Test
    @DisplayName("loginOrThrow: debe retornar token con credenciales validas")
    void loginOrThrow_ok() {
        Usuario usuario = usuarioActivo("hash");
        when(usuarioRepository.findByCorreo("ana@example.com"))
            .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123", "hash")).thenReturn(true);
        when(jwtUtils.generateToken(usuario)).thenReturn("jwt-token");

        String result = authService.loginOrThrow("ana@example.com", "Clave123");

        assertEquals("jwt-token", result);
        verify(jwtUtils).generateToken(usuario);
    }

    @Test
    @DisplayName("loginOrThrow: debe responder 401 si el usuario no existe")
    void loginOrThrow_usuarioNoExiste() {
        when(usuarioRepository.findByCorreo("ana@example.com"))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.loginOrThrow("ana@example.com", "Clave123")
        );

        assertEquals(401, exception.getStatusCode().value());
        verify(jwtUtils, never()).generateToken(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("loginOrThrow: debe responder 401 si la clave es incorrecta")
    void loginOrThrow_claveIncorrecta() {
        Usuario usuario = usuarioActivo("hash");
        when(usuarioRepository.findByCorreo("ana@example.com"))
            .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala", "hash")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.loginOrThrow("ana@example.com", "mala")
        );

        assertEquals(401, exception.getStatusCode().value());
        verify(jwtUtils, never()).generateToken(usuario);
    }

    @Test
    @DisplayName("loginOrThrow: debe responder 403 si el usuario esta inactivo")
    void loginOrThrow_usuarioInactivo() {
        Usuario usuario = usuarioActivo("hash");
        usuario.setActivo(false);
        when(usuarioRepository.findByCorreo("ana@example.com"))
            .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123", "hash")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.loginOrThrow("ana@example.com", "Clave123")
        );

        assertEquals(403, exception.getStatusCode().value());
        verify(jwtUtils, never()).generateToken(usuario);
    }

    @Test
    @DisplayName("createOrThrow: debe crear usuario y retornar token")
    void createOrThrow_conToken_ok() {
        Usuario usuario = new Usuario();
        RolUsuario rol = rol(TipoRolUsuario.CLIENTE);
        when(passwordEncoder.encode("Clave123")).thenReturn("hash");
        when(rolRepository.findByTipoRol(TipoRolUsuario.CLIENTE))
            .thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(jwtUtils.generateToken(usuario)).thenReturn("jwt-token");

        Optional<String> result = authService.createOrThrow(
            usuario,
            "Clave123",
            TipoRolUsuario.CLIENTE,
            true
        );

        assertThat(result).contains("jwt-token");
        assertEquals("hash", usuario.getClaveHash());
        assertEquals(rol, usuario.getRol());
        assertThat(usuario.getActivo()).isTrue();
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("createOrThrow: debe crear usuario sin token cuando se solicita")
    void createOrThrow_sinToken_ok() {
        Usuario usuario = new Usuario();
        RolUsuario rol = rol(TipoRolUsuario.ADMIN);
        when(passwordEncoder.encode("Clave123")).thenReturn("hash");
        when(rolRepository.findByTipoRol(TipoRolUsuario.ADMIN))
            .thenReturn(Optional.of(rol));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Optional<String> result = authService.createOrThrow(
            usuario,
            "Clave123",
            TipoRolUsuario.ADMIN,
            false
        );

        assertThat(result).isEmpty();
        verify(jwtUtils, never()).generateToken(usuario);
    }

    @Test
    @DisplayName("createOrThrow: debe responder 400 si la clave es invalida")
    void createOrThrow_claveInvalida() {
        Usuario usuario = new Usuario();

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.createOrThrow(
                usuario,
                "short",
                TipoRolUsuario.CLIENTE,
                true
            )
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    @DisplayName("createOrThrow: debe responder 500 si no existe el rol")
    void createOrThrow_rolNoExiste() {
        Usuario usuario = new Usuario();
        when(passwordEncoder.encode("Clave123")).thenReturn("hash");
        when(rolRepository.findByTipoRol(TipoRolUsuario.CLIENTE))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.createOrThrow(
                usuario,
                "Clave123",
                TipoRolUsuario.CLIENTE,
                true
            )
        );

        assertEquals(500, exception.getStatusCode().value());
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    @DisplayName("updateCredentialsOrThrow: debe actualizar correo, clave y token")
    void updateCredentialsOrThrow_ok() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo("hash");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Actual123", "hash")).thenReturn(true);
        when(passwordEncoder.encode("Nueva123")).thenReturn("new-hash");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(jwtUtils.generateToken(usuario)).thenReturn("new-token");

        String result = authService.updateCredentialsOrThrow(
            id,
            "Actual123",
            "nueva@example.com",
            "Nueva123"
        );

        assertEquals("new-token", result);
        assertEquals("nueva@example.com", usuario.getCorreo());
        assertEquals("new-hash", usuario.getClaveHash());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    @DisplayName("updateCredentialsOrThrow: debe responder 401 si la clave actual no coincide")
    void updateCredentialsOrThrow_claveActualIncorrecta() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo("hash");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala", "hash")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.updateCredentialsOrThrow(
                id,
                "mala",
                "nueva@example.com",
                "Nueva123"
            )
        );

        assertEquals(401, exception.getStatusCode().value());
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    @DisplayName("updateCredentialsOrThrow: debe responder 400 si la clave nueva es invalida")
    void updateCredentialsOrThrow_claveNuevaInvalida() {
        UUID id = UUID.randomUUID();
        Usuario usuario = usuarioActivo("hash");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Actual123", "hash")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> authService.updateCredentialsOrThrow(
                id,
                "Actual123",
                null,
                "short"
            )
        );

        assertEquals(400, exception.getStatusCode().value());
        verify(usuarioRepository, never()).save(usuario);
    }

    private static Usuario usuarioActivo(String claveHash) {
        Usuario usuario = new Usuario();
        usuario.setClaveHash(claveHash);
        usuario.setActivo(true);
        return usuario;
    }

    private static RolUsuario rol(TipoRolUsuario tipoRolUsuario) {
        RolUsuario rol = new RolUsuario();
        rol.setTipoRol(tipoRolUsuario);
        return rol;
    }
}
