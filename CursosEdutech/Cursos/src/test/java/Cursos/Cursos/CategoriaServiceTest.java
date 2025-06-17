
package Cursos.Cursos;

import Cursos.Cursos.Controller.CategoriaController;
import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.model.Curso;
import Cursos.Cursos.repository.CategoriaRepository;
import Cursos.Cursos.repository.CursoRepository;
import Cursos.Cursos.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private CategoriaCurso categoria;
    private Curso curso;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaCurso();
        categoria.setId_categoria(1L);
        categoria.setNombre_categoria("Programación");
        categoria.setCursos(new ArrayList<>());

        curso = new Curso();
        curso.setId_curso(1L);
    }

    @Test
    @DisplayName("Cuando se agrega un curso a una categoría existente, debería retornar OK")
    void agregarCursoACategoriaExistente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        ResponseEntity<?> resultado = categoriaService.agregarCursoACategoria(1L, 1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(categoriaRepository).findById(1L);
        verify(cursoRepository).findById(1L);
        verify(cursoRepository).save(any(Curso.class));

    }
    @Test
    @DisplayName("Cuando se intenta agregar un curso a una categoría inexistente, debería retornar BadRequest")
    void agregarCursoACategoriaInexistente() {

        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resultado = categoriaService.agregarCursoACategoria(99L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resultado.getStatusCode());
        verify(categoriaRepository).findById(99L);
        verify(cursoRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Cuando se guarda una categoría válida, debería retornarla")
    void guardarCategoriaValida() {

        when(categoriaRepository.save(any(CategoriaCurso.class))).thenReturn(categoria);

        CategoriaCurso resultado = categoriaService.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Programación", resultado.getNombre_categoria());
        verify(categoriaRepository).save(any(CategoriaCurso.class));
    }
    @Test
    @DisplayName("Cuando se busca una categoría existente por ID, debería retornarla")
    void obtenerCategoriaPorIdExistente() {

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<CategoriaCurso> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Programación", resultado.get().getNombre_categoria());
        verify(categoriaRepository).findById(1L);
    }
    @Test
    @DisplayName("Cuando se busca una categoría que no existe, debería retornar Optional vacío")
    void obtenerCategoriaPorIdInexistente() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CategoriaCurso> resultado = categoriaService.obtenerPorId(99L);

        assertFalse(resultado.isPresent());
        verify(categoriaRepository).findById(99L);
    }
    @Test
    @DisplayName("Debería retornar todas las categorías")
    void obtenerTodasLasCategorias() {

        List<CategoriaCurso> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<CategoriaCurso> resultado = categoriaService.obtenerTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Programación", resultado.get(0).getNombre_categoria());
        verify(categoriaRepository).findAll();
    }
    @Test
    @DisplayName("Verificar si existe una categoría por nombre")
    void verificarExistenciaCategoriaPorNombre() {

        String nombreCategoria = "Programación";
        when(categoriaRepository.existsByNombre(nombreCategoria)).thenReturn(true);

        boolean resultado = categoriaService.existeCategoria(nombreCategoria);

        assertTrue(resultado);
        verify(categoriaRepository).existsByNombre(nombreCategoria);
    }
    @Test
    @DisplayName("Eliminar categoría debería llamar al método deleteById del repositorio")
    void eliminarCategoria() {

        Long idCategoria = 1L;
        doNothing().when(categoriaRepository).deleteById(idCategoria);

        categoriaService.eliminar(idCategoria);

        verify(categoriaRepository).deleteById(idCategoria);
    }



}
