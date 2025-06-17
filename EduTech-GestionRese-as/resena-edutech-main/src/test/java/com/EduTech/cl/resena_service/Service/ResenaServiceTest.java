package com.EduTech.cl.resena_service.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.EduTech.cl.resena_service.model.Resena;
import com.EduTech.cl.resena_service.repository.ResenaRepository;
import com.EduTech.cl.resena_service.service.ResenaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resena;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setIdResena(1L);
        resena.setDescripcion("Excelente curso");
        resena.setNota(5);
        resena.setIdCurso(100L);
        resena.setIdEstudiante(200L);
    }

    @Test
    void testFindAll() {
        when(resenaRepository.findAll()).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Excelente curso", resultado.get(0).getDescripcion());
    }

    @Test
    void testFindById_Existente() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        Optional<Resena> resultado = resenaService.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(5, resultado.get().getNota());
    }

    @Test
    void testFindById_NoExistente() {
        when(resenaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Resena> resultado = resenaService.findById(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testSave() {
        when(resenaRepository.save(resena)).thenReturn(resena);

        Resena guardada = resenaService.save(resena);

        assertNotNull(guardada);
        assertEquals("Excelente curso", guardada.getDescripcion());
    }

    @Test
    void testDeleteById() {
        doNothing().when(resenaRepository).deleteById(1L);

        assertDoesNotThrow(() -> resenaService.deleteById(1L));
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByIdCurso() {
        when(resenaRepository.findByIdCurso(100L)).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.findByIdCurso(100L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(100L, resultado.get(0).getIdCurso());
    }

    @Test
    void testFindByIdEstudiante() {
        when(resenaRepository.findByIdEstudiante(200L)).thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.findByIdEstudiante(200L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(200L, resultado.get(0).getIdEstudiante());
    }

}
