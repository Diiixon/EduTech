package Cursos.Cursos.service;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.model.Curso;
import Cursos.Cursos.repository.CategoriaRepository;
import Cursos.Cursos.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CursoRepository cursoRepository;

    public ResponseEntity<?> agregarCursoACategoria(Long idCategoria, Long idCurso) {
        try {
            CategoriaCurso categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            curso.setCategoriasCurso(categoria);
            cursoRepository.save(curso);

            return ResponseEntity.ok("Curso añadido exitosamente a la categoría");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    public CategoriaCurso guardar(CategoriaCurso categoria){
            return categoriaRepository.save(categoria);
    }
    public Optional<CategoriaCurso> obtenerPorId(Long id_categoria){
        return categoriaRepository.findById(id_categoria);
    }
    public List<CategoriaCurso> obtenerTodas(){
        return categoriaRepository.findAll();
    }
    public void eliminar(Long id_categoria){
        categoriaRepository.deleteById(id_categoria);
    }
    public boolean existeCategoria(String nombre){
        return categoriaRepository.existsByNombre(nombre);
    }

}
