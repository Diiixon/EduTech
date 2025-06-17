package Cursos.Cursos.Controller;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.model.Curso;
import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/inscripciones")
public class InscripcionController {
    @Value("${servicio.usuarios.url}") // URL del microservicio de usuarios
    private String usuarioServiceUrl;
    @Autowired
    private InscripcionService inscripcionService;
    @Autowired
    private RestTemplate restTemplate;
    @Operation(summary = "Crear una nueva inscripción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Inscripción creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "400",
                    description = "Error en la solicitud - El usuario no existe o no es un Estudiante",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crearInscripcion(
            @Parameter(description = "Datos de la inscripción a crear", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = Inscripcion.class),
                            examples = @ExampleObject(value = """
                {
                    "fecha_inscripcion": "2024-01-20",
                    "estado": "ACTIVO",
                    "id_estudiante": 1,
                    "curso": 1,
                    "notas": []
                }
                """
                            )))
            @RequestBody Inscripcion inscripcion) {

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
    @Operation(summary = "Obtiene una inscripcion por su ID")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Inscripcion encontrado", content = @Content(schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "Inscripcion no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> obtenerPorId(
        @Parameter(description = "Id de la inscripcion", required = true)
        @PathVariable Long id){
        return inscripcionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Obtiene la lista de todas las inscripciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "Inscripcion no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Inscripcion> listarTodos(){
        return inscripcionService.obtenerTodos();
    }
    @Operation(summary = "Eliminar Inscripcion por id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Inscripcion eliminada",
                    content = @Content(schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "Inscripcion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la inscripción", required = true)
        @PathVariable Long id){
        inscripcionService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Obtiene lista de inscripciones por ID del estudiante")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Inscripcion encontrada",
                    content = @Content(schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "inscripcion no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estudiante/{id_estudiante}")
    public List<Inscripcion> buscarPorEstudiante(
        @Parameter(description = "ID del estudiante", required = true)
        @PathVariable Long id_estudiante){
        return inscripcionService.buscarPorEstudiante(id_estudiante);
    }
    @Operation(summary = "Obtiene categorias por rango de fechas")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Lista encontrada",
                    content = @Content(schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "Inscripciones no encontradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/fechas")
    public List<Inscripcion> buscarPorRangoFechas(
            @Parameter(description = "Fecha inicial rango", required = true)
            @RequestParam Date fechaInicio,
            @Parameter(description = "Fecha final rango", required = true)
            @RequestParam Date fechaFin){
        return inscripcionService.buscarPorRangoFechas(fechaInicio,fechaFin);
    }
    @Operation(summary = "Obtiene el número de inscripciones por curso")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Inscripciones encontradas",
                    content = @Content(schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404",description = "Inscripciones no encontradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/curso/{id_curso}/count")
    public Long contarPorCurso(
        @Parameter(description = "Id del curso", required = true)
        @PathVariable Long id_curso){
        return inscripcionService.inscripcionesPorCurso(id_curso);
    }
}
