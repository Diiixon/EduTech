package cl.duocuc.edutech.repository;

import cl.duocuc.edutech.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {


    Optional<Object> findByRut(int rut);

    @Query("SELECT COUNT(e) FROM Estudiante e")
    Integer contarTotalEstudiantes();

}
