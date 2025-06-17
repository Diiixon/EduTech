package com.EduTech.cl.pago_service.controller;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.service.CarroCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/carro")
@Tag(name = "Carro de Compra", description = "Microservicio para gestionar el carro de compras de estudiantes")
public class CarroCompraController {

    @Value("${servicio.usuarios.url}")
    private String usuarioServiceUrl;

    @Value("${servicio.cursos.url}")
    private String cursoServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CarroCompraService carroCompraService;

    private boolean validarEstudiante(Long idEstudiante) {
        try {
            String url = usuarioServiceUrl + "/" + idEstudiante;
            Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);
            return usuario != null && "Estudiante".equalsIgnoreCase((String) usuario.get("dtype"));
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> obtenerCurso(Long idCurso) {
        try {
            String url = cursoServiceUrl + "/" + idCurso;
            Map<String, Object> curso = restTemplate.getForObject(url, Map.class);
            if (curso != null && curso.get("id_curso") != null) {
                return curso;
            }
        } catch (Exception ignored) {}
        return null;
    }

    @Operation(summary = "Agregar curso al carro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso agregado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario o curso inválido")
    })
    @PostMapping("/{idEstudiante}/agregar/{idCurso}")
    public ResponseEntity<?> agregarCurso(
        @Parameter(description = "ID del estudiante", required = true)
        @PathVariable Long idEstudiante,
        @Parameter(description = "ID del curso", required = true)
        @PathVariable Long idCurso) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        Map<String, Object> cursoData = obtenerCurso(idCurso);
        if (cursoData == null) {
            return ResponseEntity.badRequest().body("El curso no existe");
        }

        String nombre = (String) cursoData.get("nombre");
        Double precio = Double.valueOf(cursoData.get("precio").toString());

        CarroCompra carro = carroCompraService.agregarCurso(idEstudiante, idCurso, nombre, precio);
        return ResponseEntity.ok(carro);
    }

    @Operation(summary = "Quitar curso del carro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso quitado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario no válido")
    })
    @DeleteMapping("/{idEstudiante}/quitar/{idCurso}")
    public ResponseEntity<?> quitarCurso(
        @Parameter(description = "ID del estudiante", required = true)
        @PathVariable Long idEstudiante,
        @Parameter(description = "ID del curso a quitar", required = true)
        @PathVariable Long idCurso) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        CarroCompra carro = carroCompraService.quitarCurso(idEstudiante, idCurso);
        return ResponseEntity.ok(carro);
    }

    @Operation(summary = "Pagar los cursos del carro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago realizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario no válido")
    })
    @PostMapping("/{idEstudiante}/pagar")
    public ResponseEntity<?> pagarCarro(
        @Parameter(description = "ID del estudiante", required = true)
        @PathVariable Long idEstudiante) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        Pago pago = carroCompraService.pagarCarro(idEstudiante);
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Ver contenido del carro de un estudiante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carro recuperado correctamente"),
        @ApiResponse(responseCode = "400", description = "Usuario no válido")
    })
    @GetMapping("/{idEstudiante}")
    public ResponseEntity<?> verCarro(
        @Parameter(description = "ID del estudiante", required = true)
        @PathVariable Long idEstudiante) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        CarroCompra carro = carroCompraService.obtenerCarro(idEstudiante);
        return ResponseEntity.ok(carro);
    }
}
