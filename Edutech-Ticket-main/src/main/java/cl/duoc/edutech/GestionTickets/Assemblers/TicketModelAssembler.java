package cl.duoc.edutech.GestionTickets.Assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import cl.duoc.edutech.GestionTickets.controller.TicketReporteController;
import cl.duoc.edutech.GestionTickets.model.TicketReporte;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class TicketModelAssembler implements RepresentationModelAssembler<TicketReporte, EntityModel<TicketReporte>> {

    @Override
    @NonNull
    public EntityModel<TicketReporte> toModel(TicketReporte ticket) {
        return EntityModel.of(ticket,
            linkTo(methodOn(TicketReporteController.class).obtenerTicketPorId(ticket.getIdTicket())).withSelfRel(),
            linkTo(methodOn(TicketReporteController.class).listarTickets()).withRel("todos-los-tickets"),
            linkTo(methodOn(TicketReporteController.class).crearTicket(null)).withRel("crear-ticket"),
            linkTo(methodOn(TicketReporteController.class).responderTicketJson(ticket.getIdTicket(), null)).withRel("responder-ticket"),
            linkTo(methodOn(TicketReporteController.class).eliminarTicket(ticket.getIdTicket())).withRel("eliminar-ticket")
        );
    }
}
