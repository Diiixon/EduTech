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

@RestController
@RequestMapping("/api/v1/resenas")
public class ResenaController {

    @Value("${usuario.service.url}")
    private String usuarioServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    public ResponseEntity<List<Resena>> listarResenas() {
        List<Resena> resenas = resenaService.findAll();
        if (resenas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resenas);
    }

    @PostMapping
    public ResponseEntity<?> registrarResena(@RequestBody Resena resena) {
        try {
            String url = usuarioServiceUrl + "/" + resena.getIdEstudiante();  // Faltaba el '+'
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


    @GetMapping("/{id}")
    public ResponseEntity<Resena> buscarPorId(@PathVariable Long id) {
        return resenaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<Resena>> buscarPorCurso(@PathVariable Long idCurso) {
        List<Resena> lista = resenaService.findByIdCurso(idCurso);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ResponseEntity<List<Resena>> buscarPorEstudiante(@PathVariable Long idEstudiante) {
        List<Resena> lista = resenaService.findByIdEstudiante(idEstudiante);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resena> actualizarResena(@PathVariable Long id, @RequestBody Resena resena) {
        return resenaService.findById(id).map(resenaExistente -> {
            resenaExistente.setDescripcion(resena.getDescripcion());
            resenaExistente.setNota(resena.getNota());
            Resena actualizada = resenaService.save(resenaExistente);
            return ResponseEntity.ok(actualizada);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        try {
            resenaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}