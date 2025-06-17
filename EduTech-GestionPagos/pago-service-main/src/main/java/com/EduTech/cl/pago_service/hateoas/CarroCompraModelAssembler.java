package com.EduTech.cl.pago_service.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.EduTech.cl.pago_service.controller.CarroCompraControllerV2;
import com.EduTech.cl.pago_service.model.CarroCompra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class CarroCompraModelAssembler implements RepresentationModelAssembler<CarroCompra, EntityModel<CarroCompra>> {

    @Override
    @NonNull
    public EntityModel<CarroCompra> toModel(CarroCompra carro) {
        return EntityModel.of(carro,
            linkTo(methodOn(CarroCompraControllerV2.class).verCarro(carro.getIdEstudiante())).withSelfRel(),
            linkTo(methodOn(CarroCompraControllerV2.class).pagarCarro(carro.getIdEstudiante())).withRel("pagar-carro"),
            linkTo(methodOn(CarroCompraControllerV2.class).quitarCurso(carro.getIdEstudiante(), null)).withRel("quitar-curso")
        );
    }

}
