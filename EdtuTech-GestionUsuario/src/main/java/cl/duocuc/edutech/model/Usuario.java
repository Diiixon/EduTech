package cl.duocuc.edutech.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Schema(description = "Id autogenerado del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Schema(description = "RUT del Usuario", example = "12345678")
    @Column(unique = true, nullable = false)
    private int rut;

    @Schema(description = "Dígito verificador del RUT", example = "K")
    @Column(nullable = false)
    private char dv;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    @Column(nullable = false)
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez Castro")
    @Column(nullable = false)
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "correo@duocuc.cl")
    @Column(unique = true, nullable = false)
    private String correo;

    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    @Column(nullable = false)
    private int telefono;

    @Schema(description = "Clave del usuario", example = "password1234")
    @Column(nullable = false)
    private String clave;

    //Setter solo para actualizar datos

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}
