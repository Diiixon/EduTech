package Cursos.Cursos.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CategoriaCurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id_categoria"
)
public class CategoriaCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id_categoria;
    @Column(unique = true, nullable = false)
    private String nombre_categoria;
    @OneToMany(mappedBy = "categoriasCurso",cascade = CascadeType.ALL)
    @JsonProperty("curso")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Curso> cursos;
}
