package Cursos.Cursos.repository;

import Cursos.Cursos.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    @Query("SELECT n FROM Nota n WHERE n.inscripcion.id_inscripcion = :idInscripcion")
    List<Nota> findByInscripcion(@Param("idInscripcion") Long idInscripcion);

}
