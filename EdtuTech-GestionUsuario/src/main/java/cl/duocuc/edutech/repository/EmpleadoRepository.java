package cl.duocuc.edutech.repository;

import cl.duocuc.edutech.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Empleado findByRut(int rut);

    Empleado findByCorreo(String correo);

}
