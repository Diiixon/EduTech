package cl.duoc.edutech.GestionTickets.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;
import cl.duoc.edutech.GestionTickets.service.TicketReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/ticketReportes")
public class TicketReporteController {

    @Autowired
    private TicketReporteService ticketReporteService;


    @Operation(summary = "Obtiene la lista de todos los tickets")
    @ApiResponse(responseCode = "200", description = "Lista de tickets obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<TicketReporte>> listarTickets() {
        List<TicketReporte> tickets = this.ticketReporteService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Crea un nuevo ticket")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ticket creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearTicket(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del ticket a crear",
            required = true,
            content = @Content(schema = @Schema(implementation = TicketReporte.class))
        )
            @RequestBody TicketReporte ticket) {
        try {
            TicketReporte nuevoTicket = ticketReporteService.save(
                    ticket.getDescripcion(),
                    ticket.getIdUsuario(),
                    ticket.getIdCurso());
            return new ResponseEntity<>(nuevoTicket, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el ticket: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtiene un ticket por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<TicketReporte>> obtenerTicketPorId(@PathVariable Long id) {
        Optional<TicketReporte> ticket = this.ticketReporteService.findById(id);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Actualiza un ticket con una respuesta y la ID del empleado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Respuesta guardada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/tickets/{id}/responder")
    public ResponseEntity<String> responderTicketJson(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {

        String respuesta = (String) datos.get("respuesta");
        Long idEmpleado;

        try {
            idEmpleado = Long.valueOf(datos.get("idEmpleado").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID de empleado inválido");
        }

        try {
            boolean actualizado = ticketReporteService.responderTicket(id, respuesta, idEmpleado);
            if (actualizado) {
                return ResponseEntity.ok("El ticket fue respondido correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el ticket con ID " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Elimina un ticket por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ticket eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        Optional<TicketReporte> ticket = ticketReporteService.findById(id);

        if (ticket.isPresent()) {
            ticketReporteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
