package com.EduTech.cl.resena_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EduTech.cl.resena_service.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    @Query("SELECT r FROM Resena r WHERE r.idCurso = :idCurso")
    List<Resena> findByIdCurso(Long idCurso);

    @Query("SELECT r FROM Resena r WHERE r.idEstudiante = :idEstudiante")
    List<Resena> findByIdEstudiante(Long idEstudiante);

}
