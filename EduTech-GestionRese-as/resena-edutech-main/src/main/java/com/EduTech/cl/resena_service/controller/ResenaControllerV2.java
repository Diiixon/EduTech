package com.EduTech.cl.resena_service.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EduTech.cl.resena_service.model.Resena;
import com.EduTech.cl.resena_service.service.ResenaService;
import com.EduTech.cl.resena_service.hateoas.ResenaModelAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api/v2/resenas")
@Tag(name = "Reseñas V2", description = "Versión 2 del microservicio de reseñas con HATEOAS")
public class ResenaControllerV2 {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ResenaModelAssembler resenaModelAssembler;

    @Operation(summary = "Listar todas las reseñas (con HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de reseñas obtenido correctamente"),
        @ApiResponse(responseCode = "204", description = "No hay reseñas disponibles")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> listarResenas() {
        List<EntityModel<Resena>> resenas = resenaService.findAll().stream()
            .map(resenaModelAssembler::toModel)
            .collect(Collectors.toList());

        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(resenas,
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withSelfRel())
        );
    }

    @Operation(summary = "Buscar una reseña por ID (con HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Resena>> buscarPorId(
        @Parameter(description = "ID de la reseña a buscar") @PathVariable Long id) {

        return resenaService.findById(id)
            .map(resenaModelAssembler::toModel)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar reseñas por ID de curso (con HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
        @ApiResponse(responseCode = "204", description = "No hay reseñas para este curso")
    })
    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> buscarPorIdCurso(
        @Parameter(description = "ID del curso") @PathVariable Long idCurso) {

        List<EntityModel<Resena>> resenas = resenaService.findByIdCurso(idCurso).stream()
            .map(resenaModelAssembler::toModel)
            .collect(Collectors.toList());

        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(resenas,
                linkTo(methodOn(ResenaControllerV2.class).buscarPorIdCurso(idCurso)).withSelfRel(),
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("resenas"))
        );
    }

    @Operation(summary = "Buscar reseñas por ID de estudiante (con HATEOAS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
        @ApiResponse(responseCode = "204", description = "No hay reseñas para este estudiante")
    })
    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> buscarPorIdEstudiante(
        @Parameter(description = "ID del estudiante") @PathVariable Long idEstudiante) {

        List<EntityModel<Resena>> resenas = resenaService.findByIdEstudiante(idEstudiante).stream()
            .map(resenaModelAssembler::toModel)
            .collect(Collectors.toList());

        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
            CollectionModel.of(resenas,
                linkTo(methodOn(ResenaControllerV2.class).buscarPorIdEstudiante(idEstudiante)).withSelfRel(),
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("resenas"))
        );
    }

    @Operation(summary = "Crear una nueva reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos para crear la reseña")
    })
    @PostMapping
    public ResponseEntity<Resena> crearResena(
        @Parameter(description = "Objeto reseña a crear") @RequestBody Resena resena) {

        Resena nueva = resenaService.save(resena);
        return ResponseEntity.ok(nueva);
    }

    @Operation(summary = "Actualizar una reseña existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(
        @Parameter(description = "ID de la reseña a actualizar") @PathVariable Long id,
        @Parameter(description = "Datos actualizados de la reseña") @RequestBody Resena resena) {

        return resenaService.findById(id)
            .map(r -> {
                r.setDescripcion(resena.getDescripcion());
                r.setNota(resena.getNota());
                r.setFecha(resena.getFecha());
                r.setIdCurso(resena.getIdCurso());
                r.setIdEstudiante(resena.getIdEstudiante());
                return ResponseEntity.ok(resenaService.save(r));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar una reseña por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(
        @Parameter(description = "ID de la reseña a eliminar") @PathVariable Long id) {

        return resenaService.findById(id)
            .map(r -> {
                resenaService.deleteById(id);
                return ResponseEntity.noContent().<Void>build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

}
