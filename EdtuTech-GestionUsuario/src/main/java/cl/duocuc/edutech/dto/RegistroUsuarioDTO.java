package cl.duocuc.edutech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para registar un nuevo usuario")
public class RegistroUsuarioDTO {

    @Schema(description = "RUT del usuario", example = "12345678")
    private int rut;

    @Schema(description = "Digito verificador", example = "K")
    private char dv;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez Castro")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "correo@duocuc.cl")
    private String correo;

    @Schema(description = "Número de teléfono del usuario", example = "987654321")
    private int telefono;

    @Schema(description = "Clave del usuario", example = "password1234")
    private String clave;

    @Schema(description = "Tipo de usuario", example = "Estudiante")
    private String tipo;

    @Schema(description = "Fecha del registro del nuevo usuario", example = "15-06-2025 17:27:00")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Schema(description = "Id del rol del empleado (profesor, operador, otros)", example = "1")
    private Integer idRol;
}
