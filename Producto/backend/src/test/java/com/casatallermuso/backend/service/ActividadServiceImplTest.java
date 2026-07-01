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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.casatallermuso.backend.entities.Actividad;
import com.casatallermuso.backend.repositories.ActividadRepository;
import com.casatallermuso.backend.services.impl.ActividadServiceImpl;

public class ActividadServiceImplTest {

    @Mock
    private ActividadRepository<TestActividad> repository;

    @InjectMocks
    private TestActividadService actividadService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        actividadService = new TestActividadService(repository);
    }

    @Test
    @DisplayName("guardar: debe guardar una actividad")
    void guardar_ok() {
        TestActividad actividad = new TestActividad();
        actividad.setNombre("Taller de Música");

        when(repository.save(actividad)).thenReturn(actividad);

        TestActividad result = actividadService.guardar(actividad);

        assertEquals("Taller de Música", result.getNombre());

        verify(repository, times(1)).save(actividad);
    }

    @Test
    @DisplayName("buscarPorID: debe retornar la actividad cuando existe")
    void buscarPorID_ok() {

        UUID id = UUID.randomUUID();

        TestActividad actividad = new TestActividad();
        actividad.setId(id);
        actividad.setNombre("Piano");

        when(repository.findById(id)).thenReturn(Optional.of(actividad));

        TestActividad result = actividadService.buscarPorID(id);

        assertEquals(id, result.getId());
        assertEquals("Piano", result.getNombre());

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("buscarPorID: debe lanzar excepción cuando no existe")
    void buscarPorID_notFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> actividadService.buscarPorID(id));

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("listar: debe retornar todas las actividades")
    void listar_ok() {

        Pageable pageable = PageRequest.of(0, 10);

        TestActividad a1 = new TestActividad();
        TestActividad a2 = new TestActividad();

        Page<TestActividad> page =
                new PageImpl<>(List.of(a1, a2));

        when(repository.findAll(pageable)).thenReturn(page);

        Page<TestActividad> result = actividadService.listar(pageable);

        assertThat(result.getContent()).hasSize(2);

        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("listarActivos: debe retornar solo actividades activas")
    void listarActivos_ok() {

        Pageable pageable = PageRequest.of(0, 10);

        TestActividad actividad = new TestActividad();
        actividad.setActivo(true);

        Page<TestActividad> page =
                new PageImpl<>(List.of(actividad));

        when(repository.findByActivo(true, pageable)).thenReturn(page);

        Page<TestActividad> result = actividadService.listarActivos(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getActivo()).isTrue();

        verify(repository).findByActivo(true, pageable);
    }

    @Test
    @DisplayName("eliminar: debe eliminar la actividad cuando existe")
    void eliminar_ok() {

        UUID id = UUID.randomUUID();

        TestActividad actividad = new TestActividad();
        actividad.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(actividad));

        actividadService.eliminar(id);

        verify(repository).delete(actividad);
    }

    @Test
    @DisplayName("eliminar: debe lanzar excepción cuando la actividad no existe")
    void eliminar_notFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> actividadService.eliminar(id));

        verify(repository).findById(id);
    }

    private static class TestActividadService
            extends ActividadServiceImpl<TestActividad, ActividadRepository<TestActividad>> {

        public TestActividadService(ActividadRepository<TestActividad> repository) {
            super(repository, "Actividad");
        }
    }

    private static class TestActividad extends Actividad {
    }
}