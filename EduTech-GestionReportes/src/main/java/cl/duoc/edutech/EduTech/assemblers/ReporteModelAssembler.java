package cl.duoc.edutech.EduTech.assemblers;

import cl.duoc.edutech.EduTech.controller.ReporteController;
import cl.duoc.edutech.EduTech.controller.ReporteControllerV2;
import cl.duoc.edutech.EduTech.model.Reporte;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReporteModelAssembler implements RepresentationModelAssembler<Reporte, EntityModel<Reporte>> {

    @Override
    public EntityModel<Reporte> toModel(Reporte reporte) {
        return EntityModel.of(reporte,
                linkTo(methodOn(ReporteControllerV2.class).reporteCursos(null)).slash("").withRel("cursos"),
                linkTo(methodOn(ReporteControllerV2.class).reporteCantidadResenas(null)).slash("").withRel("resenas"),
                linkTo(methodOn(ReporteControllerV2.class).reporteEstadoSistema(null)).slash("").withRel("sistema"),
                linkTo(methodOn(ReporteControllerV2.class).reporteEstadoTickets(null)).slash("").withRel("tickets"),
                linkTo(methodOn(ReporteControllerV2.class).totalEstudiantes(null)).slash("").withRel("estudiantes")
        );
    }
}