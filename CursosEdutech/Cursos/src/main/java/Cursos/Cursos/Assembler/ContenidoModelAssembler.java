package Cursos.Cursos.Assembler;

import Cursos.Cursos.Controller.ContenidoController;
import Cursos.Cursos.model.Contenido;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ContenidoModelAssembler implements RepresentationModelAssembler<Contenido, EntityModel<Contenido>> {

    @Override
    public EntityModel<Contenido> toModel(Contenido contenido) {
        return EntityModel.of(contenido,

                linkTo(methodOn(ContenidoController.class)
                        .obtenerPorId(contenido.getId_contenido()))
                        .withSelfRel(),


                linkTo(methodOn(ContenidoController.class)
                        .listarTodos())
                        .withRel("contenidos"),


                linkTo(methodOn(ContenidoController.class)
                        .obtenerPorId(contenido.getCurso().getId_curso()))
                        .withRel("curso"),


                linkTo(methodOn(ContenidoController.class)
                        .guardarPrueba(contenido))
                        .withRel("guardar-prueba"),


                linkTo(methodOn(ContenidoController.class)
                        .validarPrueba(contenido.getId_contenido(), null, null))
                        .withRel("validar-prueba"),

                linkTo(methodOn(ContenidoController.class)
                        .eliminar(contenido.getId_contenido()))
                        .withRel("eliminar")
        );
    }


    public CollectionModel<EntityModel<Contenido>> toCollectionModel(Iterable<? extends Contenido> contenidos) {
        List<EntityModel<Contenido>> contenidoModels = StreamSupport
                .stream(contenidos.spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(contenidoModels,
                linkTo(methodOn(ContenidoController.class).listarTodos()).withSelfRel());
    }

    public EntityModel<Contenido> toModelWithCreatedStatus(Contenido contenido) {
        EntityModel<Contenido> entityModel = toModel(contenido);
        entityModel.add(
                linkTo(methodOn(ContenidoController.class)
                        .crear(contenido))
                        .withRel("crear-similar")
        );
        return entityModel;
    }

    public EntityModel<Contenido> toModelWithTestLinks(Contenido contenido) {
        EntityModel<Contenido> entityModel = toModel(contenido);

        if (contenido.getPrueba() != null && !contenido.getPrueba().isEmpty()) {
            entityModel.add(
                    linkTo(methodOn(ContenidoController.class)
                            .guardarPrueba(contenido))
                            .withRel("actualizar-prueba"),
                    linkTo(methodOn(ContenidoController.class)
                            .validarPrueba(contenido.getId_contenido(), null, null))
                            .withRel("realizar-prueba")
            );
        }

        return entityModel;
    }
}
