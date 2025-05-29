package cl.duoc.edutech.EduTech.controller;

import cl.duoc.edutech.EduTech.model.Reporte;
import cl.duoc.edutech.EduTech.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @GetMapping("/estudiantes")
    public Reporte totalEstudiantes(@RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteTotalEstudiantes(formato);
    }

    @GetMapping("/resenas")
    public Reporte reporteCantidadResenas(@RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteResenas(formato);
    }

    @GetMapping("/tickets")
    public Reporte reporteEstadoTickets(@RequestParam(defaultValue = "texto") String formato) {
        return reporteService.generarReporteTicketsPorEstado(formato);
    }

    @GetMapping("/reporte-sistema")
    public Reporte reporteEstadoSistema(@RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteEstadoSistema(formato);
    }

    @GetMapping("/cursos")
    public Reporte reporteCursos(@RequestParam(defaultValue = "json") String formato) {
        return reporteService.generarReporteCursos(formato);
    }
}
