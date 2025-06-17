package Cursos.Cursos;

import Cursos.Cursos.model.*;
import Cursos.Cursos.repository.ContenidoRepository;
import Cursos.Cursos.repository.InscripcionRepository;
import Cursos.Cursos.repository.NotaRepository;
import Cursos.Cursos.service.ContenidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContenidoServiceTest {

    @Mock
    private ContenidoRepository contenidoRepository;

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private ContenidoService contenidoService;

    private Contenido contenido;
    private Curso curso;
    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId_curso(1L);
        curso.setNombre("Curso de Java");

        contenido = new Contenido();
        contenido.setId_contenido(1L);
        contenido.setArchivo("https://ejemplo.com/leccion1");
        contenido.setPrueba("abcdabcdab");
        contenido.setCurso(curso);

        inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(1L);
        inscripcion.setFecha_inscripcion(new Date());
        inscripcion.setEstado("ACTIVO");
        inscripcion.setId_estudiante(1L);
        inscripcion.setCurso(curso);
    }

    @Test
    @DisplayName("Debería validar respuestas del estudiante correctamente")
    void validarRespuestasEstudiante() {
        // Arrange
        when(contenidoRepository.findById(1L)).thenReturn(Optional.of(contenido));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));
        when(notaRepository.save(any(Nota.class))).thenAnswer(invocation -> {
            Nota nota = invocation.getArgument(0);
            nota.setId_nota(1L);
            return nota;
        });

        // Act
        Nota nota = contenidoService.validarRespuestasEstudiante(1L, 1L, "abcdabcdab");

        // Assert
        assertNotNull(nota);
        assertEquals(10, nota.getAciertos());
        assertEquals(70, nota.getResultado()); // 10 + (10 * 6)
        verify(contenidoRepository).findById(1L);
        verify(inscripcionRepository).findById(1L);
        verify(notaRepository).save(any(Nota.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el contenido no existe")
    void validarRespuestasContenidoNoExistente() {
        // Arrange
        when(contenidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            contenidoService.validarRespuestasEstudiante(99L, 1L, "abcdabcdab");
        });

        assertEquals("Contenido no encontrado", exception.getMessage());
        verify(contenidoRepository).findById(99L);
        verify(inscripcionRepository, never()).findById(any());
        verify(notaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería obtener todos los contenidos")
    void obtenerTodosContenidos() {
        // Arrange
        Contenido contenido2 = new Contenido();
        contenido2.setId_contenido(2L);
        contenido2.setArchivo("https://ejemplo.com/leccion2");
        contenido2.setPrueba("dcbadcbadc");

        List<Contenido> contenidos = Arrays.asList(contenido, contenido2);
        when(contenidoRepository.findAll()).thenReturn(contenidos);

        // Act
        List<Contenido> resultado = contenidoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(contenidoRepository).findAll();
    }

    @Test
    @DisplayName("Debería eliminar un contenido")
    void eliminarContenido() {
        // Act
        contenidoService.eliminar(1L);

        // Assert
        verify(contenidoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería guardar respuestas de prueba")
    void guardarRespuestasPrueba() {
        // Arrange
        Contenido contenidoConRespuestas = new Contenido();
        contenidoConRespuestas.setPrueba("abcdabcdab");
        when(contenidoRepository.save(any(Contenido.class))).thenReturn(contenidoConRespuestas);

        // Act
        Contenido resultado = contenidoService.guardarRespuestasPrueba(contenidoConRespuestas);

        // Assert
        assertNotNull(resultado);
        assertEquals("abcdabcdab", resultado.getPrueba());
        verify(contenidoRepository).save(any(Contenido.class));
    }
}