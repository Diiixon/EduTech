package com.EduTech.cl.pago_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EduTech.cl.pago_service.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

}
