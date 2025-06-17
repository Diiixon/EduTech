package cl.duoc.edutech.GestionTickets.controller;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;
import cl.duoc.edutech.GestionTickets.service.TicketReporteService;
import cl.duoc.edutech.GestionTickets.Assemblers.TicketModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v2/ticketReportes")
public class TicketReporteControllerV2 {

    @Autowired
    private TicketReporteService ticketReporteService;

    @Autowired
    private TicketModelAssembler ticketModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TicketReporte>>> listarTickets() {
        List<TicketReporte> tickets = ticketReporteService.findAll();

        List<EntityModel<TicketReporte>> ticketModels = tickets.stream()
                .map(ticketModelAssembler::toModel)
                .toList();

        CollectionModel<EntityModel<TicketReporte>> collectionModel = CollectionModel.of(ticketModels,
                Link.of("/api/v2/ticketReportes").withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TicketReporte>> obtenerTicketPorId(@PathVariable Long id) {
        Optional<TicketReporte> ticketOptional = ticketReporteService.findById(id);

        return ticketOptional
                .map(ticket -> ResponseEntity.ok(ticketModelAssembler.toModel(ticket)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearTicket(@RequestBody TicketReporte ticket) {
        try {
            TicketReporte nuevoTicket = ticketReporteService.save(
                    ticket.getDescripcion(),
                    ticket.getIdUsuario(),
                    ticket.getIdCurso());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ticketModelAssembler.toModel(nuevoTicket));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el ticket: " + e.getMessage());
        }
    }

    @PutMapping("/tickets/{id}/responder")
    public ResponseEntity<?> responderTicketJson(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
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
                Optional<TicketReporte> actualizadoTicket = ticketReporteService.findById(id);
                return actualizadoTicket
                        .map(ticket -> ResponseEntity.ok(ticketModelAssembler.toModel(ticket)))
                        .orElseGet(() -> ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el ticket con ID " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
