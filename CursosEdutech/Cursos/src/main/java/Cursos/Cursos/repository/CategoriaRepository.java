package Cursos.Cursos.repository;

import Cursos.Cursos.model.CategoriaCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaCurso, Long> {
    @Query("SELECT CASE WHEN COUNT(c)>0 THEN true ELSE false END FROM CategoriaCurso c WHERE c.nombre_categoria = :nombre")
    boolean existsByNombre(@Param("nombre")String nombre);
}
