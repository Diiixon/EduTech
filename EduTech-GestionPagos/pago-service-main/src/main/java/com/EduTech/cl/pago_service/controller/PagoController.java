package com.EduTech.cl.pago_service.controller;

import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Microservicio para gestionar pagos de cursos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Listar todos los pagos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @Operation(summary = "Buscar pago por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(
        @Parameter(description = "ID del pago a buscar", required = true)
        @PathVariable Long id) {
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un pago por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
        @Parameter(description = "ID del pago a eliminar", required = true)
        @PathVariable Long id) {
        try {
            pagoService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pago eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Pago no encontrado"));
        }
    }

    @Operation(summary = "Pagar el carro de compras de un estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago realizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
        @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/pagar/{idEstudiante}")
    public ResponseEntity<?> pagarCarro(
        @Parameter(description = "ID del estudiante que realiza el pago", required = true)
        @PathVariable Long idEstudiante,
        @Parameter(description = "Cuerpo con método de pago", required = true)
        @RequestBody Map<String, String> body) {
        try {
            String metodoPago = body.get("metodoPago");
            Pago pago = pagoService.pagarCarroCompra(idEstudiante, metodoPago);

            Map<String, Object> response = Map.of(
                    "mensaje", "Pago realizado exitosamente",
                    "pago", pago
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

}
