package cl.duoc.edutech.EduTech.controller;

import cl.duoc.edutech.EduTech.model.Reporte;
import cl.duoc.edutech.EduTech.service.ReporteService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Generar un reporte con el total de estudiantes registrados")
    @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Reporte generado correctamente",
             content = @Content(mediaType = "application/json",
             schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "500", description = "Error al generar reporte")
    })
    @GetMapping("/estudiantes")
    public Reporte totalEstudiantes(
            @Parameter(description = "Formato del reporte (json o texto)", example = "json")
            @RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteTotalEstudiantes(formato);
    }

    @Operation(summary = "Genera un reporte con la cantidad total de reseñas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generador correctamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte ")
    })
    @GetMapping("/resenas")
    public Reporte reporteCantidadResenas(
            @Parameter(description = "Formato del reporte (json o texto)", example = "json")
            @RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteResenas(formato);
    }

    @Operation(summary = "Genera un reporte del estado de los tickets (abiertos/cerrados)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generador correctamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    @GetMapping("/tickets")
    public Reporte reporteEstadoTickets(
            @Parameter(description = "Formato del reporte (json o texto)", example = "texto")
            @RequestParam(defaultValue = "texto") String formato) {
        return reporteService.generarReporteTicketsPorEstado(formato);
    }

    @Operation(summary = "Genera un reporte del estado actual del sistema (CPU, RAM, DISCO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    @GetMapping("/reporte-sistema")
    public Reporte reporteEstadoSistema(
            @Parameter(description = "Formato del reporte (json o texto)", example = "json")
            @RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteEstadoSistema(formato);
    }

    @Operation(summary = "Genera un reporte de los cursos vigentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generador correctamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Reporte.class))),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte")
    })
    @GetMapping("/cursos")
    public Reporte reporteCursos(
            @Parameter(description = "Formato del reporte (json o texto)", example = "json")
            @RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteCursos(formato);
    }
}
