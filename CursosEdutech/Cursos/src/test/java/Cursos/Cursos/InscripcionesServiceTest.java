package Cursos.Cursos;

import Cursos.Cursos.model.Curso;
import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.repository.InscripcionRepository;
import Cursos.Cursos.service.InscripcionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionesServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private InscripcionRepository inscripcionRepository;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Inscripcion inscripcion;
    private static final String USUARIOS_URL = "http://localhost:8081/api/v1/usuarios";

    @BeforeEach
    void setUp() {
        inscripcionService = new InscripcionService(inscripcionRepository, restTemplate);
        ReflectionTestUtils.setField(inscripcionService, "usuariosUrl", USUARIOS_URL);

        Curso curso = new Curso();
        curso.setId_curso(1L);

        inscripcion = new Inscripcion();
        inscripcion.setId_inscripcion(1L);
        inscripcion.setFecha_inscripcion(new Date());
        inscripcion.setEstado("ACTIVO");
        inscripcion.setId_estudiante(1L);
        inscripcion.setCurso(curso);
        inscripcion.setNotas(new ArrayList<>());
    }


    @Test
    @DisplayName("Debería guardar inscripción cuando el usuario es estudiante")
    void guardarInscripcionUsuarioValido() {
        // Arrange
        Map<String, Object> estudiante = Map.of(
                "dtype", "Estudiante",
                "nombre", "Juan Pérez",
                "correo", "juan@test.com"
        );

        when(restTemplate.getForObject(eq(USUARIOS_URL + "/1"), eq(Map.class)))
                .thenReturn(estudiante);
        when(inscripcionRepository.save(any(Inscripcion.class)))
                .thenReturn(inscripcion);

        // Act
        Inscripcion resultado = inscripcionService.guardar(inscripcion);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId_inscripcion());
        assertEquals("ACTIVO", resultado.getEstado());
        verify(inscripcionRepository).save(inscripcion);
    }

    @Test
    @DisplayName("Debería fallar al guardar cuando el usuario no es estudiante")
    void guardarInscripcionUsuarioNoEstudiante() {
        // Arrange
        Map<String, Object> profesor = Map.of(
                "dtype", "Profesor",
                "nombre", "Carlos García",
                "correo", "carlos@test.com"
        );

        when(restTemplate.getForObject(eq(USUARIOS_URL + "/1"), eq(Map.class)))
                .thenReturn(profesor);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            inscripcionService.guardar(inscripcion);
        });

        assertEquals("El usuario no existe o no es un Estudiante", exception.getMessage());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería obtener todas las inscripciones")
    void obtenerTodasInscripciones() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findAll()).thenReturn(inscripciones);

        // Act
        List<Inscripcion> resultado = inscripcionService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscripcionRepository).findAll();
    }

    @Test
    @DisplayName("Debería buscar inscripciones por rango de fechas")
    void buscarPorRangoFechas() {
        // Arrange
        Date fechaInicio = new Date();
        Date fechaFin = new Date();
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);

        when(inscripcionRepository.findByRangoFechas(fechaInicio, fechaFin))
                .thenReturn(inscripciones);

        // Act
        List<Inscripcion> resultado = inscripcionService.buscarPorRangoFechas(fechaInicio, fechaFin);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscripcionRepository).findByRangoFechas(fechaInicio, fechaFin);
    }

    @Test
    @DisplayName("Debería obtener inscripciones por estudiante")
    void buscarPorEstudiante() {
        // Arrange
        List<Inscripcion> inscripciones = Arrays.asList(inscripcion);
        when(inscripcionRepository.findByEstudiante(1L)).thenReturn(inscripciones);

        // Act
        List<Inscripcion> resultado = inscripcionService.buscarPorEstudiante(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId_estudiante());
        verify(inscripcionRepository).findByEstudiante(1L);
    }

    @Test
    @DisplayName("Debería contar inscripciones por curso")
    void contarInscripcionesPorCurso() {
        // Arrange
        when(inscripcionRepository.countByCurso(1L)).thenReturn(5L);

        // Act
        Long resultado = inscripcionService.inscripcionesPorCurso(1L);

        // Assert
        assertEquals(5L, resultado);
        verify(inscripcionRepository).countByCurso(1L);
    }
}