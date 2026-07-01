package com.casatallermuso.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.casatallermuso.backend.repositories.CursoRepository;
import com.casatallermuso.backend.repositories.EventoRepository;
import com.casatallermuso.backend.services.impl.EstadisticaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EstadisticaServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private EventoRepository eventoRepository;

    private EstadisticaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EstadisticaServiceImpl(cursoRepository, eventoRepository);
    }

    @Test
    @DisplayName("getCantidadCursosDisponibles: debe contar cursos activos")
    void getCantidadCursosDisponibles_ok() {
        when(cursoRepository.countByActivo(true)).thenReturn(4L);

        Long result = service.getCantidadCursosDisponibles();

        assertEquals(4L, result);
        verify(cursoRepository).countByActivo(true);
    }

    @Test
    @DisplayName("getCantidadEventosDisponibles: debe contar eventos activos")
    void getCantidadEventosDisponibles_ok() {
        when(eventoRepository.countByActivo(true)).thenReturn(6L);

        Long result = service.getCantidadEventosDisponibles();

        assertEquals(6L, result);
        verify(eventoRepository).countByActivo(true);
    }
}
