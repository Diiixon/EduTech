package cl.duoc.edutech.EduTech;

import cl.duoc.edutech.EduTech.model.Reporte;
import cl.duoc.edutech.EduTech.model.SistemaOshi;
import cl.duoc.edutech.EduTech.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReporteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReporteService reporteService;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        reporteService = new ReporteService(restTemplate, objectMapper);

        ReflectionTestUtils.setField(reporteService, "usuariosUrl", "http://localhost:8080/api/usuarios");
    }

    @Test
    void testGenerarReporteTotalEstudiantes() {
        Map<String,Object> estudiante = Map.of(
                "rut","12345678-9",
                "correo", "estudiante@duocuc.cl"
        );

        Map<String,Object> empleado = Map.of(
                "rut","8765432-1",
                "correo","empleado@duocuc.cl",
                "rolEmpleado", Map.of("nombre","Profesor")
        );

        Map<String,Object>[] usuarios = new Map[]{estudiante, empleado};

        when(restTemplate.getForObject(eq("http://localhost:8080/api/usuarios"), eq(Map[].class))).thenReturn(usuarios);

        Reporte reporte = reporteService.generarReporteTotalEstudiantes("texto");

        assertNotNull(reporte);
        assertEquals("Reporte Total Estudiantes", reporte.getNombre());
        assertTrue(reporte.getDescripcion().contains("2"));
        assertTrue(reporte.getDescripcion().contains("1 son estudiantes"));
        assertEquals(1, reporte.getDetalles().size());
    }

    @Test
    void testGenerarReporteResenas(){
        Map<String,Object> resena1 = Map.of(
                "id", 1,
                "comentario","Muy buen curso"
        );

        Map<String,Object> resena2 = Map.of(
                "id", 2,
                "comentario","Podria mejorar"
        );

        Map<String,Object>[] resenas = new Map[]{resena1, resena2};

        ReflectionTestUtils.setField(reporteService, "resenasUrl", "http://localhost:8083/api/v1/resenas");

        when(restTemplate.getForObject(
                eq("http://localhost:8083/api/v1/resenas"),
                eq(Map[].class)
        )).thenReturn(resenas);

        Reporte reporte = reporteService.generarReporteResenas("texto");

        assertNotNull(reporte);
        assertEquals("Reporte Total Reseñas", reporte.getNombre());
        assertTrue(reporte.getDescripcion().contains("2"));
        assertEquals(2, reporte.getDetalles().size());
    }

    @Test
    void testGenerarReporteTicketsPorEstado(){
        Map<String,Object> ticketsAbiertos = Map.of(
                "id",1,
                "estadoTicket",true
        );

        Map<String,Object> ticketsCerrados = Map.of(
                "id",2,
                "estadoTicket",false
        );

        Map<String, Object>[] tickets = new Map[]{ticketsAbiertos, ticketsCerrados};

        ReflectionTestUtils.setField(reporteService, "ticketsUrl", "http://localhost:8083/api/v1/tickets");

        when(restTemplate.getForObject(
                eq(("http://localhost:8083/api/v1/tickets")),
                eq(Map[].class)
        )).thenReturn(tickets);

        Reporte reportado = reporteService.generarReporteTicketsPorEstado("texto");

        assertNotNull(reportado);
        assertEquals("Reporte Estado de Tickets", reportado.getNombre());
        assertTrue(reportado.getDescripcion().contains("Abierto: 1"));
        assertTrue(reportado.getDescripcion().contains("Cerrado: 1"));
        assertEquals(2, reportado.getDetalles().size());
    }

    @Test
    void testGenerarReporteCursos(){
        Map<String, Object> curso1 = Map.of(
                "id", 1,
                "nombre", "Java Básico",
                "precio", 100
        );

        Map<String,Object> curso2 = Map.of(
                "id",2,
                "nombre", "Spring Boot Avanzado",
                "precio", 150
        );

        Map<String,Object>[] cursos = new Map[]{curso1, curso2};

        ReflectionTestUtils.setField(reporteService, "cursosUrl", "http://localhost:8083/api/v1/curso");

        when(restTemplate.getForObject(
                eq("http://localhost:8083/api/v1/curso"),
                eq(Map[].class)
        )).thenReturn(cursos);

        Reporte reporte = reporteService.generarReporteCursos("texto");

        assertNotNull(reporte);
        assertEquals("Reporte de Cursos Vigentes", reporte.getNombre());
        assertTrue(reporte.getDescripcion().contains("2"));
        assertEquals(2, reporte.getDetalles().size());
    }

    @Test
    void testGenerarReporteEstadoSistema(){
        Reporte reporteMock = new Reporte(
                "Reporte de Estado del Sistema",
                "Descripción del estado del sistema",
                List.of(Map.of("CPU","Intel","RAM","16GB"))
        );

        try (MockedStatic<SistemaOshi> mockStatic = Mockito.mockStatic(SistemaOshi.class)) {
            mockStatic.when(() -> SistemaOshi.generarReporteSistema(anyLong()))
                    .thenReturn(reporteMock);

            Reporte reporte = reporteService.generarReporteEstadoSistema("texto");

            assertNotNull(reporte);
            assertEquals("Reporte de Estado del Sistema", reporte.getNombre());
            assertTrue(reporte.getDescripcion().contains("estado del sistema"));
            assertEquals(1, reporte.getDetalles().size());
        }
    }

}



