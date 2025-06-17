package cl.duoc.edutech.GestionTickets.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket_reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @Column(nullable = false)
    private LocalDateTime fechaTicket = LocalDateTime.now();

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Boolean estado = false;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_empleado")
    private Long idEmpleado;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "id_curso", nullable = true)
    private Long idCurso;

    // Constructor para crear ticket desde el lado del usuario
    public TicketReporte(String descripcion, Long idUsuario, Long idCurso) {
    this.descripcion = descripcion;
    this.idUsuario = idUsuario;
    this.idCurso = idCurso;
    this.idEmpleado = null;
    this.respuesta = null;
}


    // Constructor para actualizar ticket desde el lado del empleado
    public TicketReporte(Long idTicket, String respuesta, boolean estado, Long idEmpleado) {
        this.idTicket = idTicket;
        this.respuesta = respuesta;
        this.estado = estado;
        this.idEmpleado = idEmpleado;
    }
}
