package Cursos.Cursos.Controller;

import Cursos.Cursos.model.CategoriaCurso;
import Cursos.Cursos.model.Curso;
import Cursos.Cursos.service.CursoService;
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
@RequestMapping("/api/v1/cursos")
public class CursoController {
    @Autowired
    private CursoService cursoService;
    @Operation(summary = "Crear un nuevo curso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente",content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(type ="string",example = "El nombre del curso es obligatorio"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Curso> crear(@Parameter(description = "Datos del curso a crear", required = true)
               @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Curso.class), examples = @ExampleObject(
               value = """
                    {
                        "nombre_categoria": "Programación",
                        "cursos": []
                    }
                    """
               )))
            @RequestBody Curso curso){
        return ResponseEntity.ok(cursoService.guardar(curso));
    }
    @Operation(summary = "Obtiene un Curso por su ID")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Curso encontrado", content = @Content(schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404",description = "Curso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Curso> obtenerPorId(@PathVariable Long id){
        return cursoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Obtiene la lista de todos los cursos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Lista obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404",description = "Inscripcion no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")

    })

    @GetMapping
    public List<Curso> listarTodos(){
        return cursoService.obtenerTodos();
    }
    @Operation(summary = "Eliminar Curso por id")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Curso eliminada",
                    content = @Content(schema = @Schema(implementation = Curso.class))),
            @ApiResponse(responseCode = "404",description = "Curso no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "Id del Curso", required = true)
        @PathVariable Long id){
        cursoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Buscar cursos por rango de precio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Curso.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Rango de precios inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/precio")
    public List<Curso> buscarPorRangoPrecio(
            @Parameter(description = "Precio mínimo del curso", required = true, example = "100")
            @RequestParam Integer precioMin,
            @Parameter(description = "Precio máximo del curso", required = true, example = "1000")
            @RequestParam Integer precioMax) {
            return cursoService.buscarPorPrecio(precioMin, precioMax);
    }
    @Operation(summary = "Buscar cursos por categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados por categoría", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Curso.class, type = "array"))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "ID de categoría inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/categoria/{id_Categoria}")
    public List<Curso> buscarPorCategoria(
            @Parameter(description = "ID de la categoría a buscar", required = true, example = "1")
            @PathVariable Long id_Categoria) {
        return cursoService.buscarPorCategoria(id_Categoria);
    }
    @Operation(summary = "Buscar cursos por palabra clave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cursos encontrados que contienen la palabra clave",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Curso.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Palabra de búsqueda inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{palabra}")
    public List<Curso> buscarPorPalabra(
            @Parameter(description = "Palabra clave para buscar en los cursos", required = true, example = "java")
            @PathVariable String palabra) {
        return cursoService.buscarCursosPorPalabra(palabra);
    }
}
