package com.EduTech.cl.pago_service.repository;

import com.EduTech.cl.pago_service.model.CarroCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarroCompraRepository extends JpaRepository<CarroCompra, Long> {
    Optional<CarroCompra> findByidEstudianteAndPagadoFalse(Long idEstudiante);
}
