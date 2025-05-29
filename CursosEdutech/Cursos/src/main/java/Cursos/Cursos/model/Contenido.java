package Cursos.Cursos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Contenido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id_contenido"
)
public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_contenido;
    @Column(length = 200,nullable = false)
    private String archivo;
    @Column(nullable = false)
    private String prueba;
    @ManyToOne
    @JoinColumn(name = "id_curso")
    @JsonProperty("curso")
    @JsonIdentityReference(alwaysAsId = true)
    private Curso curso;
}