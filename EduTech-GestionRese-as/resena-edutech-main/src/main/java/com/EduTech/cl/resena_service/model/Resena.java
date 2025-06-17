package com.EduTech.cl.resena_service.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resena")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idResena;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(length = 100)
    private String descripcion;

    private int nota;

    @Column(name = "id_curso")
    private Long idCurso;

    @Column(name = "id_estudiante")
    private Long idEstudiante;

    // Setters por si se modifica algo

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

}
