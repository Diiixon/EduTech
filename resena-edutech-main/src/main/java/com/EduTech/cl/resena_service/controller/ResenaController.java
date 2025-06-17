package com.EduTech.cl.resena_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EduTech.cl.resena_service.model.Resena;
import com.EduTech.cl.resena_service.service.ResenaService;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/resenas")
@Tag(name = "Reseñas", description = "Microservicio para gestionar reseñas de cursos")
public class ResenaController {

    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Listar todas las reseñas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente"),
        @ApiResponse(responseCode = "204", description = "No hay reseñas disponibles")
    })
    @GetMapping
    public ResponseEntity<List<Resena>> listarResenas() {
        List<Resena> resenas = resenaService.findAll();
        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resenas);
    }

    @Operation(summary = "Registrar una nueva reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Error al registrar la reseña o el usuario no es válido")
    })
    @PostMapping
    public ResponseEntity<?> registrarResena(
        @Parameter(description = "Objeto Resena con los datos para registrar", required = true)
        @RequestBody Resena resena) {
        try {
            String url = usuarioServiceUrl + "/" + resena.getIdEstudiante();
            Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);

            if (usuario == null || !"Estudiante".equalsIgnoreCase((String) usuario.get("dtype"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario no existe o no es un Estudiante");
            }

            Resena nueva = resenaService.save(resena);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo validar el estudiante: " + e.getMessage());
        }
    }

    @Operation(summary = "Buscar reseña por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(
        @Parameter(description = "ID de la reseña a buscar", required = true)
        @PathVariable Long id) {
        return resenaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar reseñas por ID de curso")
    @ApiResponse(responseCode = "200", description = "Listado de reseñas del curso")
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<Resena>> buscarPorCurso(
        @Parameter(description = "ID del curso para filtrar las reseñas", required = true)
        @PathVariable Long idCurso) {
        List<Resena> lista = resenaService.findByIdCurso(idCurso);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar reseñas por ID de estudiante")
    @ApiResponse(responseCode = "200", description = "Listado de reseñas del estudiante")
    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<Resena>> buscarPorEstudiante(
        @Parameter(description = "ID del estudiante para filtrar las reseñas", required = true)
        @PathVariable Long idEstudiante) {
        List<Resena> lista = resenaService.findByIdEstudiante(idEstudiante);
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Actualizar una reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(
        @Parameter(description = "ID de la reseña a actualizar", required = true)
        @PathVariable Long id,
        @Parameter(description = "Objeto Resena con los datos actualizados", required = true)
        @RequestBody Resena resena) {
        return resenaService.findById(id).map(resenaExistente -> {
            resenaExistente.setDescripcion(resena.getDescripcion());
            resenaExistente.setNota(resena.getNota());
            Resena actualizada = resenaService.save(resenaExistente);
            return ResponseEntity.ok(actualizada);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(
        @Parameter(description = "ID de la reseña a eliminar", required = true)
        @PathVariable Long id) {
        try {
            resenaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}