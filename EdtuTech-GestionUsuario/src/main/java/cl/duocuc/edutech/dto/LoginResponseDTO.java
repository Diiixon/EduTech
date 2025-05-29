package cl.duocuc.edutech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private int rut;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
}
