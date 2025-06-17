package com.EduTech.cl.pago_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "curso_carro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoCarro {

    @Id
    private Long idCurso;

    private String nombre;

    private double precio;
}

