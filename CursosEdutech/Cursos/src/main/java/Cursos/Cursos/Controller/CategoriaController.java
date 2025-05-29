package Cursos.Cursos.Controller;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    @PostMapping("/{idCategoria}/cursos/{idCurso}")
    public ResponseEntity<?> agregarCursoACategoria(
            @PathVariable Long idCategoria,
            @PathVariable Long idCurso) {
        return categoriaService.agregarCursoACategoria(idCategoria, idCurso);
    }
    @PostMapping
    public ResponseEntity<CategoriaCurso> crear(@RequestBody CategoriaCurso categoria){
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaCurso> obtenerPorId(@PathVariable Long id){
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<CategoriaCurso> listarTodos(){
        return categoriaService.obtenerTodas();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> existePorNombre(@PathVariable String nombre){
        return ResponseEntity.ok(categoriaService.existeCategoria(nombre));
    }

}
