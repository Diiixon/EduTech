package com.EduTech.cl.pago_service.controller;

import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.service.PagoService;
import com.EduTech.cl.pago_service.hateoas.PagoModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pagos")
@Tag(name = "Pagos V2", description = "Versión 2 del microservicio de pagos con soporte HATEOAS")
public class PagoControllerV2 {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoModelAssembler assembler;

    @Operation(summary = "Listar todos los pagos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listarPagos() {
        List<EntityModel<Pago>> pagos = pagoService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(pagos,
                        linkTo(methodOn(PagoControllerV2.class).listarPagos()).withSelfRel()));
    }

    @Operation(summary = "Buscar un pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> buscarPorId(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            pagoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Pago no encontrado"));
        }
    }

}
