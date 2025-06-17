package com.EduTech.cl.pago_service.hateoas;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.EduTech.cl.pago_service.controller.PagoControllerV2;
import com.EduTech.cl.pago_service.model.Pago;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<Pago, EntityModel<Pago>> {

    @Override
    @NonNull
    public EntityModel<Pago> toModel(Pago pago) {
        return EntityModel.of(pago,
            linkTo(methodOn(PagoControllerV2.class).buscarPorId(pago.getIdPago())).withSelfRel(),
            linkTo(methodOn(PagoControllerV2.class).listarPagos()).withRel("todos-los-pagos"),
            linkTo(methodOn(PagoControllerV2.class).eliminar(pago.getIdPago())).withRel("eliminar-pago")
        );
    }

}
