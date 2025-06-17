package Cursos.Cursos.Controller;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.service.CategoriaService;
import Cursos.Cursos.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private InscripcionService inscripcionService;

    @Operation(summary = "Agrega un curso existente a una categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso agregado a categoria",
                 content = @Content(mediaType = "application/json", schema = @Schema(type = "string", example = "Curso agregado exitosamente a categoria"))),
            @ApiResponse(responseCode = "404", description = "Curso o categoria no encontrada"),
            @ApiResponse(responseCode = "400",description = "Error en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(type ="string",example = "Categoria/Curso no encontrad@"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{idCategoria}/cursos/{idCurso}")
    public ResponseEntity<?> agregarCursoACategoria(
            @Parameter(description = "Id de la categoria", required = true)
            @PathVariable Long idCategoria,
            @Parameter(description = "Id del curso", required = true)
            @PathVariable Long idCurso) {
        return categoriaService.agregarCursoACategoria(idCategoria, idCurso);
    }
    @Operation(summary = "Crear una nueva categoría de curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria creada exitosamente",content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaCurso.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(type ="string",example = "El nombre de la categoria es obligatorio"))),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor")

    })


    @PostMapping
    public ResponseEntity<CategoriaCurso> crear(@Parameter(description = "Datos de la categoría a crear", required = true)
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = CategoriaCurso.class), examples = @ExampleObject(
                value = """
                {
                    "nombre_categoria": "Programación",
                    "cursos": []
                }
                """
                )))
            @RequestBody CategoriaCurso categoria
    ) {
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }
    @Operation(summary = "Obtiene una categoria por su ID")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Categoria encontrada",
                    content = @Content(schema = @Schema(implementation = CategoriaCurso.class))),
            @ApiResponse(responseCode = "404",description = "Categoria no encontrado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor")

    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaCurso> obtenerPorId(@PathVariable Long id){
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Obtiene la lista de todas las categorias")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaCurso.class))),
            @ApiResponse(responseCode = "404",description = "Inscripcion no encontrado"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor")

    })

    @GetMapping
    public List<CategoriaCurso> listarTodos(){
        return categoriaService.obtenerTodas();
    }
    @Operation(summary = "Eliminar categoria por id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Categoria eliminada",
                    content = @Content(schema = @Schema(implementation = CategoriaCurso.class))),
            @ApiResponse(responseCode = "404",description = "Categoria no encontrada"),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor")

    })
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la Categoria", required = true)
            @PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.ok().build();
    }


}
