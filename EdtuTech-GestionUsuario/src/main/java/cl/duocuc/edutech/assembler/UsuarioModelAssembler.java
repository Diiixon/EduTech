package cl.duocuc.edutech.assembler;

import cl.duocuc.edutech.controller.UsuarioController;
import cl.duocuc.edutech.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        Long idLong = Long.valueOf(usuario.getId());

        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(idLong)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).recuperarClave(usuario.getCorreo())).withRel("recuperar-clave")
        );
    }
}