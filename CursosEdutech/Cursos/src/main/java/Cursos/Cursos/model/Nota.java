package Cursos.Cursos.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Nota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Long id_nota;
    @Column(nullable = false)
    private Integer aciertos;
    @Column(nullable = false)
    private Integer resultado;
    @Column(nullable = false, length = 1000)
    private String detalleEvaluacion;
    @ManyToOne
    @JoinColumn(name = "id_inscripcion")
    @JsonProperty("inscripcion")
    @JsonIdentityReference(alwaysAsId = true)
    private Inscripcion inscripcion;

}
