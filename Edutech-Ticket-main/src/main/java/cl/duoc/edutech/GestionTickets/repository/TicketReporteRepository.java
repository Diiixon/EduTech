package cl.duoc.edutech.GestionTickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;

@Repository
public interface TicketReporteRepository extends JpaRepository<TicketReporte, Long> {

    /*@Query("SELECT r FROM TicketReporte WHERE r.id_usuario = :id_usuario")
    List<TicketReporte> findByIdUsuario(int idUsuario);*/

    @Query("SELECT r FROM TicketReporte r WHERE r.idTicket = :idTicket")
    List<TicketReporte> findByTicket(Long idTicket);

}
