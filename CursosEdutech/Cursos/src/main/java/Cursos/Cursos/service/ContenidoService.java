package Cursos.Cursos.service;

import Cursos.Cursos.model.Contenido;
import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.model.Nota;
import Cursos.Cursos.repository.ContenidoRepository;
import Cursos.Cursos.repository.InscripcionRepository;
import Cursos.Cursos.repository.NotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContenidoService {
    @Autowired
    private ContenidoRepository contenidoRepository;
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private NotaRepository notaRepository;

    public Contenido guardar(Contenido contenido){
        return contenidoRepository.save(contenido);
    }
    public Optional<Contenido> obtenerPorId(Long id_contenido){
        return contenidoRepository.findById(id_contenido);
    }
    public List<Contenido> obtenerTodos(){
        return contenidoRepository.findAll();
    }
    public void eliminar(Long id_contenido){
        contenidoRepository.deleteById(id_contenido);
    }
    public Contenido guardarRespuestasPrueba(Contenido contenido) {
        // Validar que la prueba tenga exactamente 10 respuestas
        if (contenido.getPrueba() == null || contenido.getPrueba().length() != 10) {
            throw new IllegalArgumentException("La prueba debe contener exactamente 10 respuestas");
        }

        // Validar que solo contenga letras 'a', 'b', 'c' o 'd'
        if (!contenido.getPrueba().matches("[abcd]{10}")) {
            throw new IllegalArgumentException("Las respuestas solo pueden ser 'a', 'b', 'c' o 'd'");
        }

        return contenidoRepository.save(contenido);
    }

    //Método para validar respuestas de un estudiante
    public Nota validarRespuestasEstudiante(Long contenidoId, Long inscripcionId, String respuestasEstudiante) {
        // Validar formato de respuestas del estudiante
        if (respuestasEstudiante == null || respuestasEstudiante.length() != 10) {
            throw new IllegalArgumentException("Las respuestas del estudiante deben contener exactamente 10 respuestas");
        }

        if (!respuestasEstudiante.matches("[abcd]{10}")) {
            throw new IllegalArgumentException("Las respuestas solo pueden ser 'a', 'b', 'c' o 'd'");
        }

        // Obtener el contenido con las respuestas correctas
        Contenido contenido = contenidoRepository.findById(contenidoId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        // Obtener la inscripción
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));

        // Validar que la inscripción corresponda al curso del contenido
        if (!inscripcion.getCurso().getId_curso().equals(contenido.getCurso().getId_curso())) {
            throw new IllegalArgumentException("La inscripción no corresponde al curso del contenido");
        }

        // Comparar respuestas y calcular puntuación
        String respuestasCorrectas = contenido.getPrueba();
        int aciertos = 0;
        StringBuilder detalle = new StringBuilder();
        String respuestasCurrectas = contenido.getPrueba();

        for (int i = 0; i < 10; i++) {
            if (respuestasCorrectas.charAt(i) == respuestasEstudiante.charAt(i)) {
                aciertos++;
                detalle.append("Pregunta ").append(i + 1).append(": Correcta");
            } else {
                detalle.append("Pregunta ").append(i + 1)
                        .append(": Incorrecta (Tu respuesta: ")
                        .append(respuestasEstudiante.charAt(i))
                        .append(", Respuesta correcta: ")
                        .append(respuestasCorrectas.charAt(i))
                        .append(")");
            }
        }

        // Crear y guardar la nota
        Nota nota = new Nota();
        nota.setAciertos(aciertos);
        nota.setResultado(10 + (aciertos * 6));
        nota.setDetalleEvaluacion(detalle.toString());
        nota.setInscripcion(inscripcion);
        try{
            return notaRepository.save(nota);
        }   catch(Exception e){
            throw new RuntimeException("Error al guardar la nota" + e.getMessage());
        }

    }


}
