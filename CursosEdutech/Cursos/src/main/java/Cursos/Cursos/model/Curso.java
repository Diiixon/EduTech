package Cursos.Cursos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id_curso"
)
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_curso;
    @Column(unique = true, length = 50, nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Integer precio;
    @JsonProperty("contenidos")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "curso",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contenido> contenidos;
    @JsonProperty("inscripciones")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    @JsonProperty("categoria")
    @JsonIdentityReference(alwaysAsId = true)
    private CategoriaCurso categoriasCurso;
}
