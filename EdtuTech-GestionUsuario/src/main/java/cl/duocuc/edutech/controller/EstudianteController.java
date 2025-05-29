package cl.duocuc.edutech.controller;

import cl.duocuc.edutech.service.EstudianteService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    // En EstudianteController del microservicio gestion-usuarios
    @GetMapping("/total")
    public Integer contarTotalEstudiantes() {
        return estudianteService.contarTotalEstudiantes();
    }
}
