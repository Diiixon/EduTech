package Cursos.Cursos.repository;

import Cursos.Cursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CursoRepository extends JpaRepository<Curso, Long> {
    //buscar curso por nombre
    @Query("SELECT c FROM Curso c WHERE c.nombre = :nombre")
    Optional<Curso> findByNombre(@Param("nombre")String nombre);
    //Buscar por rango de precio
    @Query("SELECT c FROM Curso c WHERE c.precio BETWEEN :precioMin AND :precioMax")
    List<Curso> findByRangoPrecio(@Param("precioMin")Integer precioMin, @Param("precioMax") Integer precioMax);
    //Buscar por categoria
    @Query("SELECT c FROM Curso c WHERE c.categoriasCurso.id_categoria = :idCategoria")
    List<Curso> findByCategoriaId(@Param("idCategoria") Long idCategoria);
    //Buscar curso que contenga x palabra
    @Query("SELECT c FROM Curso c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%',:palabra,'%'))")
    List<Curso> findCursosByNombreContaining(@Param("palabra")String palabra);


    Integer precio(Integer precio);
}
