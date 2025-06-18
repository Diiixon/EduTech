package cl.duoc.edutech.GestionTickets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;
import cl.duoc.edutech.GestionTickets.repository.TicketReporteRepository;
import cl.duoc.edutech.GestionTickets.service.TicketReporteService;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TicketReporteServiceTest {
    
    @InjectMocks
    private TicketReporteService ticketService;

    @Mock
    TicketReporteRepository ticketRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup(){
        ticketService.urlUsuarios = "http://localhost:8081/api/v1/usuarios";
        ticketService.urlCursos = "http://localhost:8082/api/v1/cursos";
    }

    @Test
    @DisplayName("Con idUsuario e idCurso válidos, debería guardar Ticket")
    void guardarTicket(){
        Long idUsuario = 1L;
        Long idCurso = 2L;
        String descripcion = "Error al cargar curso";

        when(restTemplate.getForEntity(anyString(), eq(Void.class)))
            .thenReturn(mock(org.springframework.http.ResponseEntity.class));

        when(restTemplate.getForObject(contains("cursos"), eq(Map.class)))
            .thenReturn(Map.of());

        TicketReporte mockTicket = new TicketReporte(descripcion, idUsuario, idCurso);
        when(ticketRepository.save(any(TicketReporte.class)))
            .thenReturn(mockTicket);
            
        TicketReporte resultado = ticketService.save(descripcion, idUsuario, idCurso);

        assertNotNull(resultado);
        assertEquals(descripcion, resultado.getDescripcion());
        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Si el idUsuario es inválido, debería lanzar excepción")
    void usuarioInvalido(){
        Long idUsuario = 99L;
        when(restTemplate.getForEntity(contains("/" + idUsuario), eq(Void.class)))
            .thenThrow(HttpClientErrorException.NotFound.class);

            assertThrows(IllegalArgumentException.class, () -> {
                ticketService.save("Error", idUsuario, null);
            }); 
    }

    @Test
    @DisplayName("Al responder el ticket, si el empleado existe y su rol es válido, deberia actualizarse el ticket")
    void responderTicket(){
        Long idTicket = 1L;
        Long idEmpleado = 10L;
        String respuesta = "Solucionado";

        TicketReporte mockTicket = new TicketReporte("Error", 1L, null);
        mockTicket.setIdTicket(idTicket);

        when(ticketRepository.findById(idTicket)).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any())).thenReturn(mockTicket);

        Map<String, String> mockEmpleado = Map.of("dtype", "Empleado", "rol", "Operador Soporte");
        when(restTemplate.getForObject(contains("/" + idEmpleado), eq(Map.class))).thenReturn(mockEmpleado);

        boolean resultado = ticketService.responderTicket(idTicket, respuesta, idEmpleado);

        assertTrue(resultado);
        assertTrue(mockTicket.getEstado());
        assertEquals(idEmpleado, mockTicket.getIdEmpleado());
    }

    @Test
    @DisplayName("Al responder el ticket, si el idEmpleado no existe o no pertene al rol correspondiente, debe lanzar error")
    void empleadoInvalido(){
        Long idTicket = 1L;
        Long idEmpleado = 99L;

        when(restTemplate.getForObject(contains("/" + idEmpleado), eq(Map.class)))
            .thenReturn(Map.of("dtype", "Estudiante", "rol", "ALUMNO"));

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.responderTicket(idTicket, "respuesta", idEmpleado);
        });     
    }
}
