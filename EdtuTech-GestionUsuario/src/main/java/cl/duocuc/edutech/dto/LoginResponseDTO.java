package cl.duocuc.edutech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    @Schema(description = "RUT del usuario", example = "12345678")
    private int rut;

    @Schema(description = "Nombres del usuario", example = "Juan Carlos")
    private String nombres;

    @Schema(description = "Apellidos del usuario", example = "Pérez Castro")
    private String apellidos;

    @Schema(description = "Correo electrónico del usuario", example = "correo@duocuc.cl")
    private String correo;

    @Schema(description = "Id del rol del empleado (profesor, operador, otros)", example = "1")
    private String rol;
}
