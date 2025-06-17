package com.EduTech.cl.pago_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carro_compra")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarroCompra;

    @Column(name = "idEstudiante")
    private Long idEstudiante;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_carro_compra")

    private List<CursoCarro> cursos = new ArrayList<>();

    private boolean pagado;
}
