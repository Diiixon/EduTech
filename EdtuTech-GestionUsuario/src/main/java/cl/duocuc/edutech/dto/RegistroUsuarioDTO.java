package cl.duocuc.edutech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioDTO {

    private int rut;
    private char dv;
    private String nombres;
    private String apellidos;
    private String correo;
    private int telefono;
    private String clave;
    private String tipo;
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private Integer idRol;
}
