package cl.duocuc.edutech.controller;

import cl.duocuc.edutech.service.EstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Contar Estudiantes", description = "Retorna la cantidad total de estudiantes registrado en el sistema")
    @ApiResponse(responseCode = "200", description = "Conteo total de estudiantes retornado exitosamente")
    @GetMapping("/total")
    public Integer contarTotalEstudiantes() {
        return estudianteService.contarTotalEstudiantes();
    }
}
