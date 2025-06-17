package com.EduTech.cl.resena_service.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.EduTech.cl.resena_service.controller.ResenaControllerV2;
import com.EduTech.cl.resena_service.model.Resena;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component	
public class ResenaModelAssembler implements RepresentationModelAssembler<Resena, EntityModel<Resena>> {

    @Override
    @NonNull
    public EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(resena,
            // Link a esta misma reseña (GET)
            linkTo(methodOn(ResenaControllerV2.class).buscarPorId(resena.getIdResena())).withSelfRel(),

            // Link para obtener todas las reseñas (GET)
            linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("todas-las-resenas"),

            // Link para crear una nueva reseña (POST)
            linkTo(methodOn(ResenaControllerV2.class).crearResena(null)).withRel("crear-resena"),

            // Link para actualizar esta reseña (PUT)
            linkTo(methodOn(ResenaControllerV2.class).actualizarResena(resena.getIdResena(), null)).withRel("actualizar-resena"),

            // Link para eliminar esta reseña (DELETE)
            linkTo(methodOn(ResenaControllerV2.class).eliminarResena(resena.getIdResena())).withRel("eliminar-resena"),

            // Link para buscar todas las reseñas de un curso específico
            linkTo(methodOn(ResenaControllerV2.class).buscarPorIdCurso(resena.getIdCurso())).withRel("resenas-por-curso"),

            // Link para buscar todas las reseñas de un estudiante específico
            linkTo(methodOn(ResenaControllerV2.class).buscarPorIdEstudiante(resena.getIdEstudiante())).withRel("resenas-por-estudiante")
        );
    }

}
