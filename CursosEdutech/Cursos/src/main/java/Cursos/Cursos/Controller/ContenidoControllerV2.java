package Cursos.Cursos.Controller;

import Cursos.Cursos.Assembler.ContenidoModelAssembler;
import Cursos.Cursos.model.Contenido;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v2/contenidos")
public class ContenidoControllerV2 {

    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private ContenidoModelAssembler assembler;

    @Operation(summary = "Crear un nuevo contenido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contenido creado exitosamente",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Contenido>> crear(
            @Parameter(description = """
            Datos del contenido a crear. Debe incluir:
            - archivo: contenido del curso
            - prueba: respuestas de la prueba
            - curso: ID del curso al que pertenece
            """, required = true)
            @RequestBody Contenido contenido) {
        Contenido nuevoContenido = contenidoService.guardar(contenido);
        EntityModel<Contenido> entityModel = assembler.toModelWithCreatedStatus(nuevoContenido);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @Operation(summary = "Obtiene contenido por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido encontrado",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public EntityModel<Contenido> obtenerPorId(
            @Parameter(description = "ID del contenido", required = true)
            @PathVariable Long id) {
        Contenido contenido = contenidoService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contenido no encontrado"));
        return assembler.toModel(contenido);
    }

    @Operation(summary = "Obtiene la lista de todos los contenidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public CollectionModel<EntityModel<Contenido>> listarTodos() {
        List<Contenido> contenidos = contenidoService.obtenerTodos();
        return assembler.toCollectionModel(contenidos);
    }

    @Operation(summary = "Eliminar contenido por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contenido eliminado"),
            @ApiResponse(responseCode = "404", description = "Contenido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del contenido", required = true)
            @PathVariable Long id) {
        contenidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Guardar respuestas de una prueba")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prueba guardada exitosamente",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "400", description = "Formato de prueba inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/prueba")
    public ResponseEntity<EntityModel<Contenido>> guardarPrueba(
            @Parameter(description = "10 respuestas (abcd)", required = true)
            @RequestBody Contenido contenido) {
        try {
            Contenido contenidoConPrueba = contenidoService.guardarRespuestasPrueba(contenido);
            EntityModel<Contenido> entityModel = assembler.toModelWithTestLinks(contenidoConPrueba);
            return ResponseEntity.ok(entityModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Validar respuestas de una prueba y generar nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prueba validada exitosamente",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "400", description = "Formato de respuestas inválido o prueba/inscripción no encontrada"),
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
