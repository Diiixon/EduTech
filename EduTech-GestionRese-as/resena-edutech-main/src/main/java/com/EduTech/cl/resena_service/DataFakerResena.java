package com.EduTech.cl.resena_service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.EduTech.cl.resena_service.model.Resena;
import com.EduTech.cl.resena_service.service.ResenaService;

import net.datafaker.Faker;

@Component
public class DataFakerResena implements CommandLineRunner {

    private final ResenaService resenaService;

    public DataFakerResena(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 3; i++) {  // Solo se crearán 3 reseñas
            Resena resena = new Resena();

            int nota = random.nextInt(5) + 1; // nota entre 1 y 5
            resena.setNota(nota);

            String descripcion;

            switch (nota) {
                case 1:
                    descripcion = "Muy mala experiencia, no lo recomiendo.";
                    break;
                case 2:
                    descripcion = "Tuvo partes buenas, pero en general fue decepcionante.";
                    break;
                case 3:
                    descripcion = "Regular, podría mejorar en varios aspectos.";
                    break;
                case 4:
                    descripcion = "Buen curso, cumplió mis expectativas.";
                    break;
                case 5:
                    descripcion = "Excelente curso, aprendí muchísimo.";
                    break;
                default:
                    descripcion = "Sin comentarios.";
                    break;
            }

            resena.setDescripcion(descripcion);
            resena.setFecha(LocalDateTime.now());
            resena.setIdCurso((long) random.nextInt(5) + 1);
            resena.setIdEstudiante((long) random.nextInt(5) + 1);

            resenaService.save(resena);
}

    }
}
