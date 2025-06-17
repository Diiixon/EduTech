
package Cursos.Cursos;

import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.repository.NotaRepository;
import Cursos.Cursos.service.NotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotasServiceTest {

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private NotaService notaService;

    private Nota nota1;
    private Nota nota2;
    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {

        inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(1L);

        nota1 = new Nota();
        nota1.setId_nota(1L);
        nota1.setAciertos(8);
        nota1.setResultado(56);
        nota1.setDetalleEvaluacion("Respuestas correctas: 1,2,3,4,5,6,7,8 - Incorrectas: 9,10");
        nota1.setInscripcion(inscripcion);


        nota2 = new Nota();
        nota2.setId_nota(2L);
        nota2.setAciertos(10);
        nota2.setResultado(70);
        nota2.setDetalleEvaluacion("Respuestas correctas: 1,2,3,4,5,6,7,8,9,10 - Incorrectas: ninguna");
        nota2.setInscripcion(inscripcion);
    }

    @Test
    @DisplayName("Cuando existen notas para una inscripción, debería retornar la lista de notas con todos sus atributos")
    void obtenerNotasPorInscripcionExistente() {

        Long idInscripcion = 1L;
        List<Nota> notasEsperadas = Arrays.asList(nota1, nota2);
        when(notaRepository.findByInscripcion(idInscripcion)).thenReturn(notasEsperadas);

        List<Nota> resultado = notaService.obtenerNotasPorInscripcion(idInscripcion);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        Nota primeraNota = resultado.get(0);
        assertAll("Verificar atributos de la primera nota",
                () -> assertEquals(1L, primeraNota.getId_nota()),
                () -> assertEquals(8, primeraNota.getAciertos()),
                () -> assertEquals(56, primeraNota.getResultado()),
                () -> assertTrue(primeraNota.getDetalleEvaluacion().contains("Respuestas correctas")),
                () -> assertEquals(1L, primeraNota.getInscripcion().getId_inscripcion())
        );

        Nota segundaNota = resultado.get(1);
        assertAll("Verificar atributos de la segunda nota",
                () -> assertEquals(2L, segundaNota.getId_nota()),
                () -> assertEquals(10, segundaNota.getAciertos()),
                () -> assertEquals(70, segundaNota.getResultado()),
                () -> assertTrue(segundaNota.getDetalleEvaluacion().contains("Respuestas correctas")),
                () -> assertEquals(1L, segundaNota.getInscripcion().getId_inscripcion())
        );
    }

    @Test
    @DisplayName("Verificar que los aciertos estén en el rango correcto (0-10)")
    void verificarRangoAciertos() {

        Long idInscripcion = 1L;
        nota1.setAciertos(0);
        nota2.setAciertos(10);
        List<Nota> notasEsperadas = Arrays.asList(nota1, nota2);
        when(notaRepository.findByInscripcion(idInscripcion)).thenReturn(notasEsperadas);

        List<Nota> resultado = notaService.obtenerNotasPorInscripcion(idInscripcion);

        assertAll("Verificar rango de aciertos",
                () -> assertTrue(resultado.get(0).getAciertos() >= 0),
                () -> assertTrue(resultado.get(0).getAciertos() <= 10),
                () -> assertTrue(resultado.get(1).getAciertos() >= 0),
                () -> assertTrue(resultado.get(1).getAciertos() <= 10)
        );
    }

    @Test
    @DisplayName("Verificar que los resultados estén en el rango correcto (10-70)")
    void verificarRangoResultados() {

        Long idInscripcion = 1L;
        nota1.setResultado(10);
        nota2.setResultado(70);
        List<Nota> notasEsperadas = Arrays.asList(nota1, nota2);
        when(notaRepository.findByInscripcion(idInscripcion)).thenReturn(notasEsperadas);

        List<Nota> resultado = notaService.obtenerNotasPorInscripcion(idInscripcion);

        assertAll("Verificar rango de resultados",
                () -> assertTrue(resultado.get(0).getResultado() >= 10),
                () -> assertTrue(resultado.get(0).getResultado() <= 70),
                () -> assertTrue(resultado.get(1).getResultado() >= 10),
                () -> assertTrue(resultado.get(1).getResultado() <= 70)
        );
    }

    @Test
    @DisplayName("Cuando no existen notas para una inscripción, debería retornar lista vacía")
    void obtenerNotasPorInscripcionSinNotas() {

        Long idInscripcion = 99L;
        when(notaRepository.findByInscripcion(idInscripcion)).thenReturn(Collections.emptyList());

        List<Nota> resultado = notaService.obtenerNotasPorInscripcion(idInscripcion);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(notaRepository).findByInscripcion(idInscripcion);
    }
}
