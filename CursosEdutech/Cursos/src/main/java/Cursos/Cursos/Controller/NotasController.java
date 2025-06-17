package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
public class NotasController {
    @Autowired
    private NotaService notaService;

    @Operation(summary = "Obtener notas de un alumno por inscripcion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notas encontradas",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inscripcion.class))),
            @ApiResponse(responseCode = "404", description = "Inscripcion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/inscripcion/{idInscripcion}")
    public ResponseEntity<List<Nota>> obtenerNotasPorInscripcion(@PathVariable Long idInscripcion){
        List<Nota> notas = notaService.obtenerNotasPorInscripcion(idInscripcion);
        return ResponseEntity.ok(notas);
    }
}
