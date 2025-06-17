package cl.duocuc.edutech.repository;

import cl.duocuc.edutech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.rut = :rut")
    void deleteByRut(@Param("rut") int rut);

    @Query("SELECT u FROM Usuario u WHERE u.rut = :rut")
    Usuario findByRut(@Param("rut") Integer rut);

    Usuario findByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo and u.clave = :clave")
    Usuario iniciarSesion(@Param("correo") String correo,@Param("clave") String clave);

}


