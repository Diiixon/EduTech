package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Curso;
import Cursos.Cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;
    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso){
        return ResponseEntity.ok(cursoService.guardar(curso));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id){
        return cursoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<Curso> listarTodos(){
        return cursoService.obtenerTodos();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        cursoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/buscar/precio")
    public List<Curso> buscarPorRangoPrecio(
            @RequestParam Integer precioMin,
            @RequestParam Integer precioMax){
        return cursoService.buscarPorPrecio(precioMin,precioMax);
    }
    @GetMapping("/categoria/{id_Categoria}")
    public List<Curso> buscarPorCategoria(@PathVariable Long id_Categoria){
        return cursoService.buscarPorCategoria(id_Categoria);
    }
    @GetMapping("/buscar/{palabra}")
    public List<Curso> buscarPorPalabra(@PathVariable String palabra){
        return cursoService.buscarCursosPorPalabra(palabra);
    }

}
