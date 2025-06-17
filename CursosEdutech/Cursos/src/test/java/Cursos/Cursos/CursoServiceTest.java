package Cursos.Cursos;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.model.Contenido;
import Cursos.Cursos.model.Curso;
import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.repository.CursoRepository;
import Cursos.Cursos.service.CursoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private CategoriaCurso categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaCurso();
        categoria.setId_categoria(1L);
        categoria.setNombre_categoria("Programación");

        curso = new Curso();
        curso.setId_curso(1L);
        curso.setNombre("Java Básico");
        curso.setPrecio(100);
        curso.setContenidos(new ArrayList<>());
        curso.setInscripciones(new ArrayList<>());
        curso.setCategoriasCurso(categoria);
    }

    @Test
    @DisplayName("Debería guardar un curso correctamente")
    void guardarCurso() {

        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso resultado = cursoService.guardar(curso);

        assertNotNull(resultado);
        assertEquals("Java Básico", resultado.getNombre());
        assertEquals(100, resultado.getPrecio());
        verify(cursoRepository).save(curso);
    }

    @Test
    @DisplayName("Debería obtener un curso por ID")
    void obtenerCursoPorId() {

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        Optional<Curso> resultado = cursoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Java Básico", resultado.get().getNombre());
        verify(cursoRepository).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando no existe el curso")
    void obtenerCursoNoExistente() {

        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Curso> resultado = cursoService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
        verify(cursoRepository).findById(99L);
    }

    @Test
    @DisplayName("Debería obtener todos los cursos")
    void obtenerTodosCursos() {

        Curso curso2 = new Curso();
        curso2.setId_curso(2L);
        curso2.setNombre("Python Básico");
        curso2.setPrecio(150);

        List<Curso> cursos = Arrays.asList(curso, curso2);
        when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(cursoRepository).findAll();
    }

    @Test
    @DisplayName("Debería eliminar un curso")
    void eliminarCurso() {

        cursoService.eliminar(1L);

        verify(cursoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debería buscar cursos por rango de precio")
    void buscarCursosPorRangoPrecio() {

        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByRangoPrecio(50, 150)).thenReturn(cursos);

        List<Curso> resultado = cursoService.buscarPorPrecio(50, 150);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100, resultado.get(0).getPrecio());
        verify(cursoRepository).findByRangoPrecio(50, 150);
    }

    @Test
    @DisplayName("Debería buscar cursos por categoría")
    void buscarCursosPorCategoria() {

        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByCategoriaId(1L)).thenReturn(cursos);

        List<Curso> resultado = cursoService.buscarPorCategoria(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Programación", resultado.get(0).getCategoriasCurso().getNombre_categoria());
        verify(cursoRepository).findByCategoriaId(1L);
    }

    @Test
    @DisplayName("Debería buscar cursos por palabra clave")
    void buscarCursosPorPalabra() {

        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findCursosByNombreContaining("Java")).thenReturn(cursos);

        List<Curso> resultado = cursoService.buscarCursosPorPalabra("Java");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getNombre().contains("Java"));
        verify(cursoRepository).findCursosByNombreContaining("Java");
    }
}