package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    @Value("${servicio.usuarios.url}") // URL del microservicio de usuarios
    private String usuarioServiceUrl;
    @Autowired
    private InscripcionService inscripcionService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> crearInscripcion(@RequestBody Inscripcion inscripcion) {
        try {
            String url = usuarioServiceUrl + "/" + inscripcion.getId_estudiante();
            Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);

            if (usuario == null || !"Estudiante".equalsIgnoreCase((String) usuario.get("dtype"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario no existe o no es un Estudiante");
            }

            Inscripcion nueva = inscripcionService.guardar(inscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo validar el estudiante: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> obtenerPorId(@PathVariable Long id){
        return inscripcionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<Inscripcion> listarTodos(){
        return inscripcionService.obtenerTodos();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        inscripcionService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/estudiante/{id_estudiante}")
    public List<Inscripcion> buscarPorEstudiante(@PathVariable Long id_estudiante){
        return inscripcionService.buscarPorEstudiante(id_estudiante);
    }
    @GetMapping("/fechas")
    public List<Inscripcion> buscarPorRangoFechas(
            @RequestParam Date fechaInicio,
            @RequestParam Date fechaFin){
        return inscripcionService.buscarPorRangoFechas(fechaInicio,fechaFin);
    }
    @GetMapping("/curso/{id_curso}/count")
    public Long contarPorCurso(@PathVariable Long id_curso){
        return inscripcionService.inscripcionesPorCurso(id_curso);
    }
}
