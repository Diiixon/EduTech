package cl.duocuc.edutech.repository;

import cl.duocuc.edutech.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findById(Integer id);
}
