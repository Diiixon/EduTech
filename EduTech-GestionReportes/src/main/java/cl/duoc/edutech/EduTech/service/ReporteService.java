package cl.duoc.edutech.EduTech.service;

import cl.duoc.edutech.EduTech.model.Reporte;
import cl.duoc.edutech.EduTech.model.SistemaOshi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReporteService {
    @Value("${usuarios.service.url}")
    private String usuariosUrl;

    @Value("${inscripciones.service.url}")
    private String inscripcionesUrl;

    @Value("${resenas.service.url}")
    private String resenasUrl;

    @Value("${tickets.service.url}")
    private String ticketsUrl;

    @Value("${compras.service.url}")
    private String comprasUrl;

    @Value("${cursos.service.url}")
    private String cursosUrl;


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final AtomicLong contador = new AtomicLong(1);

    public ReporteService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Reporte generarReporteTotalEstudiantes(String formato) {
        try {
            Map<String, Object>[] estudiantes = restTemplate.getForObject(usuariosUrl, Map[].class);
            int total = estudiantes != null ? estudiantes.length : 0;


            List<Map<String, Object>> listaEstudiantes = new ArrayList<>();
            if(estudiantes != null) {
                for (Map<String, Object> usuario : estudiantes) {
                    if(!usuario.containsKey("rolEmpleado")){
                        listaEstudiantes.add(usuario);
                    }
                }
            }

            String descripcion = "Total de usuarios registrados: " + total +
                    ", de los cuales " + listaEstudiantes.size() + " son estudiantes.";

            Reporte reporte = new Reporte(
                    "Reporte Total Estudiantes",
                    descripcion,
                    listaEstudiantes

            );

            //mostrarReporte(reporte, formato);
            return reporte;

        } catch (Exception e) {
            System.err.println("Error al generar reporte: " + e.getMessage());
            return null;
        }
    }

    public Reporte generarReporteResenas(String formato) {
        try {
            Map<String, Object>[] resenas = restTemplate.getForObject(resenasUrl, Map[].class);
            int total = resenas != null ? resenas.length : 0;

            String descripcion = "Total de reseñas registradas: " + total;

            List<Map<String, Object>> listaResenas = resenas != null ? List.of(resenas) : List.of();

            Reporte reporte = new Reporte(
                    "Reporte Total Reseñas",
                    descripcion,
                    listaResenas
            );

            //mostrarReporte(reporte, formato);
            return reporte;

        } catch (Exception e) {
            System.err.println("Error al generar reporte de reseñas: " + e.getMessage());
            return null;
        }
    }

    public Reporte generarReporteTicketsPorEstado(String formato) {
        try {
            Map<String, Object>[] tickets = restTemplate.getForObject(ticketsUrl, Map[].class);

            int abiertos = 0;
            int cerrados = 0;

            if (tickets != null) {
                for (Map<String, Object> ticket : tickets) {
                    Object estadoObj = ticket.get("estadoTicket");

                    if (estadoObj instanceof Boolean) {
                        boolean estado = (Boolean) estadoObj;
                        if (estado) {
                            abiertos++;
                        } else {
                            cerrados++;
                        }
                    } else if (estadoObj instanceof String) {
                        if ("true".equalsIgnoreCase((String) estadoObj)) {
                            abiertos++;
                        } else {
                            cerrados++;
                        }
                    }
                }
            }

                    String descripcion = String.format("Tickets en estado Abierto: %d, Cerrado: %d", abiertos, cerrados);

            List<Map<String, Object>> listaTickets = tickets != null ? List.of(tickets) : List.of();

            Reporte reporte = new Reporte(
                    "Reporte Estado de Tickets",
                    descripcion,
                    listaTickets
            );

            //mostrarReporte(reporte, formato);
            return reporte;

        } catch (Exception e) {
            System.err.println("Error al generar reporte de tickets: " + e.getMessage());
            return null;
        }
    }

    public Reporte generarReporteEstadoSistema(String formato) {
        try {
            Reporte reporte = SistemaOshi.generarReporteSistema(contador.getAndIncrement());
            if ("texto".equalsIgnoreCase(formato)) {
            }
            return reporte;
        } catch (Exception e) {
            System.err.println("Error al generar reporte de estado del sistema: " + e.getMessage());
            return null;
        }
    }

    public Reporte generarReporteCursos(String formato) {
    try {
        Map<String, Object>[] cursos = restTemplate.getForObject(cursosUrl, Map[].class);
        int total = cursos != null ? cursos.length : 0;

        String descripcion = "Total de cursos vigentes: " + total;

        List<Map<String, Object>> listaCursos = cursos != null ? List.of(cursos) : List.of();

        Reporte reporte = new Reporte(
                "Reporte de Cursos Vigentes",
                descripcion,
                listaCursos
        );

        return reporte;

    } catch (Exception e) {
        System.err.println("Error al generar reporte de cursos: " + e.getMessage());
        return null;
    }
}


}