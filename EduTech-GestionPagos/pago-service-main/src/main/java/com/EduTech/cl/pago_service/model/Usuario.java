package com.EduTech.cl.pago_service.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String tipo;

}
