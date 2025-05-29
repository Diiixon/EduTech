package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Contenido;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.service.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {
    @Autowired
    private ContenidoService contenidoService;

    @PostMapping
    public ResponseEntity<Contenido> crear(@RequestBody Contenido contenido) {
        return ResponseEntity.ok(contenidoService.guardar(contenido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contenido> obtenerPorId(@PathVariable Long id) {
        return contenidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Contenido> listarTodos() {
        return contenidoService.obtenerTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        contenidoService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/prueba")
    public ResponseEntity<Contenido> guardarPrueba(@RequestBody Contenido contenido) {
        try {
            return ResponseEntity.ok(contenidoService.guardarRespuestasPrueba(contenido));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{idContenido}/inscripciones/{idInscripcion}/validar-prueba")
    public ResponseEntity<Nota> validarPrueba(
            @PathVariable Long idContenido,
            @PathVariable Long idInscripcion,
            @RequestBody String respuestasEstudiante) {
        try {
            Nota nota = contenidoService.validarRespuestasEstudiante(
                    idContenido,
                    idInscripcion,
                    respuestasEstudiante
            );
            return ResponseEntity.ok(nota);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
