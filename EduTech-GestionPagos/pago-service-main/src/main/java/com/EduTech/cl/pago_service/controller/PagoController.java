package com.EduTech.cl.pago_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.service.PagoService;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    /*@PostMapping
    public ResponseEntity<Pago> registrarPago(@RequestBody Pago pago) {
        return ResponseEntity.ok(pagoService.save(pago));
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            pagoService.deleteById(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pago eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Pago no encontrado"));
        }
    }

    @PostMapping("/pagar/{idEstudiante}")
    public ResponseEntity<?> pagarCarro(@PathVariable Long idEstudiante, @RequestBody Map<String, String> body) {
        try {
            String metodoPago = body.get("metodoPago");
            Pago pago = pagoService.pagarCarroCompra(idEstudiante, metodoPago);

            Map<String, Object> response = Map.of(
                    "mensaje", "Pago realizado exitosamente",
                    "pago", pago);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Ocurrió un error inesperado"));
        }
    }

}
