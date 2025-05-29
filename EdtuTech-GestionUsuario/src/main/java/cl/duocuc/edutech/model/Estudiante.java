package cl.duocuc.edutech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estudiante extends Usuario {

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("Fecha de Generación")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

}
