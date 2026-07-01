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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.InscripcionCurso;
import com.casatallermuso.backend.entities.Usuario;
import com.casatallermuso.backend.repositories.InscripcionCursoRepository;
import com.casatallermuso.backend.services.impl.InscripcionCursoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class InscripcionCursoServiceImplTest {

    @Mock
    private InscripcionCursoRepository repository;

    private InscripcionCursoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new InscripcionCursoServiceImpl(repository);
    }

    @Test
    @DisplayName("findById: debe delegar en el repositorio")
    void findById_ok() {
        UUID id = UUID.randomUUID();
        InscripcionCurso inscripcion = InscripcionCurso.builder().build();
        when(repository.findById(id)).thenReturn(Optional.of(inscripcion));

        Optional<InscripcionCurso> result = service.findById(id);

        assertThat(result).contains(inscripcion);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("findByActividad: debe retornar pagina de inscripciones")
    void findByActividad_ok() {
        Curso curso = new Curso();
        Pageable pageable = PageRequest.of(0, 10);
        var page = new PageImpl<InscripcionCurso>(List.of(InscripcionCurso.builder().build()));
        when(repository.findByActividad(curso, pageable)).thenReturn(page);

        var result = service.findByActividad(curso, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findByActividad(curso, pageable);
    }

    @Test
    @DisplayName("findByUsuario: debe retornar pagina de inscripciones")
    void findByUsuario_ok() {
        Usuario usuario = new Usuario();
        Pageable pageable = PageRequest.of(0, 10);
        var page = new PageImpl<InscripcionCurso>(List.of(InscripcionCurso.builder().build()));
        when(repository.findByUsuario(usuario, pageable)).thenReturn(page);

        var result = service.findByUsuario(usuario, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findByUsuario(usuario, pageable);
    }

    @Test
    @DisplayName("inscribirUsuario: debe guardar inscripcion cuando hay cupos")
    void inscribirUsuario_conCupos_ok() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        curso.setNombre("Piano");
        curso.setCupos(2);
        when(repository.countByActividad(curso)).thenReturn(1L);

        service.inscribirUsuario(usuario, curso);

        ArgumentCaptor<InscripcionCurso> captor =
            ArgumentCaptor.forClass(InscripcionCurso.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getUsuario()).isSameAs(usuario);
        assertThat(captor.getValue().getActividad()).isSameAs(curso);
    }

    @Test
    @DisplayName("inscribirUsuario: debe lanzar excepcion cuando no hay cupos")
    void inscribirUsuario_sinCupos() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        curso.setNombre("Piano");
        curso.setCupos(1);
        when(repository.countByActividad(curso)).thenReturn(1L);

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> service.inscribirUsuario(usuario, curso)
        );

        assertThat(exception.getMessage()).contains("no tiene cupos disponibles");
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("eliminarInscripcion: debe eliminar cuando existe")
    void eliminarInscripcion_ok() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        InscripcionCurso inscripcion = InscripcionCurso.builder()
            .usuario(usuario)
            .actividad(curso)
            .build();
        when(repository.findByUsuarioAndActividad(usuario, curso))
            .thenReturn(Optional.of(inscripcion));

        service.eliminarInscripcion(usuario, curso);

        verify(repository).delete(inscripcion);
    }

    @Test
    @DisplayName("eliminarInscripcion: debe responder 404 cuando no existe")
    void eliminarInscripcion_notFound() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        when(repository.findByUsuarioAndActividad(usuario, curso))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> service.eliminarInscripcion(usuario, curso)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(repository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("isUsuarioInscrito: debe retornar true si existe inscripcion")
    void isUsuarioInscrito_true() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        when(repository.findByUsuarioAndActividad(usuario, curso))
            .thenReturn(Optional.of(InscripcionCurso.builder().build()));

        assertThat(service.isUsuarioInscrito(usuario, curso)).isTrue();
    }

    @Test
    @DisplayName("isUsuarioInscrito: debe retornar false si no existe inscripcion")
    void isUsuarioInscrito_false() {
        Usuario usuario = new Usuario();
        Curso curso = new Curso();
        when(repository.findByUsuarioAndActividad(usuario, curso))
            .thenReturn(Optional.empty());

        assertThat(service.isUsuarioInscrito(usuario, curso)).isFalse();
    }

    @Test
    @DisplayName("getCuposRestantes: debe restar cupos tomados")
    void getCuposRestantes_ok() {
        Curso curso = new Curso();
        curso.setCupos(8);
        when(repository.countByActividad(curso)).thenReturn(3L);

        Long result = service.getCuposRestantes(curso);

        assertEquals(5L, result);
    }
}
