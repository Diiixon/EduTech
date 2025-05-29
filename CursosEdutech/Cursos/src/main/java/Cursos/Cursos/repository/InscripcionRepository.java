package Cursos.Cursos.repository;

import Cursos.Cursos.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion,Long> {
    //Revisar inscripcion
    @Query("SELECT i FROM Inscripcion i WHERE i.id_estudiante = :idEstudiante")
    List<Inscripcion> findByEstudiante(@Param("idEstudiante")Long idEstudiante);
    //Buscar inscripciones por rango de fechas
    @Query("SELECT i FROM Inscripcion i WHERE i.fecha_inscripcion BETWEEN :fechaInicio AND :fechaFin")
    List<Inscripcion> findByRangoFechas(@Param("fechaInicio")Date fechaInicio, @Param("fechaFin")Date fechaFin);
    //Contar inscripciones por curso
    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id_curso = :idCurso")
    Long countByCurso(@Param("idCurso")Long idCurso);

}
