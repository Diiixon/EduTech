package com.EduTech.cl.pago_service.controller;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.service.CarroCompraService;
import com.EduTech.cl.pago_service.hateoas.CarroCompraModelAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v2/carro")
@Tag(name = "Carro de compra V2", description = "Versión 2 del carro con soporte HATEOAS")
public class CarroCompraControllerV2 {

    @Autowired
    private CarroCompraService carroCompraService;

    @Autowired
    private CarroCompraModelAssembler assembler;

    @Operation(summary = "Ver carro por estudiante")
    @GetMapping("/{idEstudiante}")
    public EntityModel<CarroCompra> verCarro(@PathVariable Long idEstudiante) {
        CarroCompra carro = carroCompraService.obtenerCarro(idEstudiante);
        return assembler.toModel(carro);
    }

    @Operation(summary = "Pagar un carro")
    @PostMapping("/{idEstudiante}/pagar")
    public EntityModel<CarroCompra> pagarCarro(@PathVariable Long idEstudiante) {
        carroCompraService.pagarCarro(idEstudiante);
        CarroCompra carro = carroCompraService.obtenerCarro(idEstudiante);
        return assembler.toModel(carro);
    }

    @Operation(summary = "Quitar curso del carro (simulado para HATEOAS)")
    @DeleteMapping("/{idEstudiante}/quitar/{idCurso}")
    public ResponseEntity<?> quitarCurso(@PathVariable Long idEstudiante, @PathVariable Long idCurso) {
        CarroCompra carro = carroCompraService.quitarCurso(idEstudiante, idCurso);
        return ResponseEntity.ok(assembler.toModel(carro));
    }

}
