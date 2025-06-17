package cl.duocuc.edutech;

import cl.duocuc.edutech.repository.EstudianteRepository;
import cl.duocuc.edutech.service.EstudianteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testContarTotalEstudiantes(){
        int totalEsperado = 42;

        when(estudianteRepository.contarTotalEstudiantes()).thenReturn(totalEsperado);

        Integer resultado = estudianteService.contarTotalEstudiantes();

        assertNotNull(resultado);
        assertEquals(totalEsperado, resultado);
        verify(estudianteRepository, times(1)).contarTotalEstudiantes();
    }
}
