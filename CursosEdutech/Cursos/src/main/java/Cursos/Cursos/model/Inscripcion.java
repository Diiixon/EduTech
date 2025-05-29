package Cursos.Cursos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Inscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id_inscripcion"
)
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_inscripcion;
    @Column(nullable = false)
    private Date fecha_inscripcion;
    @Column(nullable = false)
    private String estado;
    @Column(unique = true, nullable = false)
    private Long id_estudiante;
    @ManyToOne
    @JoinColumn(name = "id_curso")
    @JsonProperty("curso")
    @JsonIdentityReference(alwaysAsId = true)
    private Curso curso;
    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL)
    @JsonProperty("notas")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Nota> notas;

}
