package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Contenido;
import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contenidos")
public class ContenidoController {
    @Autowired
    private ContenidoService contenidoService;

    @Operation(summary = "Crear un nuevo contenido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contenido.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Contenido> crear(
        @Parameter(description = """
        Datos del contenido a crear. Debe incluir:
        - archivo: contenido del curso
        - prueba: respuestas de la prueba
        - curso: ID del curso al que pertenece
        """, required = true)
        @RequestBody Contenido contenido) {
        return ResponseEntity.ok(contenidoService.guardar(contenido));
    }
    @Operation(summary = "Obtiene contenido por su ID")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "contenido encontrado", content = @Content(schema = @Schema(implementation = Contenido.class))),
            @ApiResponse(responseCode = "404",description = "contenido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Contenido> obtenerPorId(
        @Parameter(description = "Id de contenido", required = true)
        @PathVariable Long id) {
        return contenidoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Obtiene la lista de todos los contenidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contenido.class))),
            @ApiResponse(responseCode = "404",description = "contenidos no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Contenido> listarTodos() {
        return contenidoService.obtenerTodos();
    }
    @Operation(summary = "Eliminar contenido por id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "contenido eliminado",
                    content = @Content(schema = @Schema(implementation = Contenido.class))),
            @ApiResponse(responseCode = "404",description = "Contenido no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID del contenido", required = true)
        @PathVariable Long id) {
        contenidoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Guardar respuestas de una prueba")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prueba guardada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Contenido.class))),
            @ApiResponse(responseCode = "400", description = "Formato de prueba inválido", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/prueba")
    public ResponseEntity<Contenido> guardarPrueba(
        @Parameter(description = "10 respuestas (abcd)", required = true)
        @RequestBody Contenido contenido) {
        try {
            return ResponseEntity.ok(contenidoService.guardarRespuestasPrueba(contenido));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(summary = "Validar respuestas de una prueba y generar nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prueba validada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Nota.class))),
            @ApiResponse(responseCode = "400", description = "Formato de respuestas inválido o prueba/inscripción no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Contenido o inscripción no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping("/{idContenido}/inscripciones/{idInscripcion}/validar-prueba")
    public ResponseEntity<Nota> validarPrueba(
            @Parameter(description = "ID del contenido que contiene la prueba", required = true)
            @PathVariable Long idContenido,

            @Parameter(description = "ID de la inscripción del estudiante", required = true)
            @PathVariable Long idInscripcion,

            @Parameter(description = """
        String de 10 caracteres con las respuestas del estudiante.
        Reglas:
        - Debe contener exactamente 10 respuestas
        - Solo se permiten las letras a, b, c, d en minúsculas
        - Las respuestas deben estar en el orden correspondiente
        """, required = true)
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
