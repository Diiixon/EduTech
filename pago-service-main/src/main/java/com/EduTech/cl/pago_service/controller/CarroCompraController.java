package com.EduTech.cl.pago_service.controller;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.service.CarroCompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/carro")
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

            // Validamos que venga el campo "id_curso" y que no sea null
            if (curso != null && curso.get("id_curso") != null) {
                return curso;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Podrías loguear el error para depuración
            return null;
        }
    }


    // 1. Agregar curso al carro
    @PostMapping("/{idEstudiante}/agregar/{idCurso}")
    public ResponseEntity<?> agregarCurso(
            @PathVariable Long idEstudiante,
            @PathVariable Long idCurso) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        Map<String, Object> cursoData = obtenerCurso(idCurso);
        if (cursoData == null) {
            return ResponseEntity.badRequest().body("El curso no existe");
        }

        // Extraemos los datos necesarios
        String nombre = (String) cursoData.get("nombre");
        Double precio = Double.valueOf(cursoData.get("precio").toString());

        // Llamas al servicio para agregar el curso al carro
        CarroCompra carro = carroCompraService.agregarCurso(idEstudiante, idCurso, nombre, precio);
        return ResponseEntity.ok(carro);

    }

    // 2. Quitar curso del carro
    @DeleteMapping("/{idEstudiante}/quitar/{idCurso}")
    public ResponseEntity<?> quitarCurso(
            @PathVariable Long idEstudiante,
            @PathVariable Long idCurso) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        CarroCompra carro = carroCompraService.quitarCurso(idEstudiante, idCurso);
        return ResponseEntity.ok(carro);
    }

    // 3. Pagar el carro
    @PostMapping("/{idEstudiante}/pagar")
    public ResponseEntity<?> pagarCarro(@PathVariable Long idEstudiante) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }

        Pago pago = carroCompraService.pagarCarro(idEstudiante);
        return ResponseEntity.ok(pago);
    }

    // 4. (Opcional) Ver el contenido del carro
    @GetMapping("/{idEstudiante}")
    public ResponseEntity<?> verCarro(@PathVariable Long idEstudiante) {

        if (!validarEstudiante(idEstudiante)) {
            return ResponseEntity.badRequest().body("El usuario no existe o no es un Estudiante");
        }
        CarroCompra carro = carroCompraService.obtenerCarro(idEstudiante);
        return ResponseEntity.ok(carro);
    }
}
