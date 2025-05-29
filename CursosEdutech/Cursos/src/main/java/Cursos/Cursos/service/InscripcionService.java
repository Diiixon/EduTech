package Cursos.Cursos.service;

import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InscripcionService {
    @Autowired
    private InscripcionRepository inscripcionRepository;

    public Inscripcion guardar(Inscripcion inscripcion){
        return inscripcionRepository.save(inscripcion);
    }
    public Optional<Inscripcion> obtenerPorId(Long id_inscripcion){
        return inscripcionRepository.findById(id_inscripcion);
    }
    public List<Inscripcion> obtenerTodos(){
        return inscripcionRepository.findAll();
    }
    public void eliminar(Long id_inscripcion){
        inscripcionRepository.deleteById(id_inscripcion);
    }
    public List<Inscripcion> buscarPorEstudiante(Long id_estudiante){
        return inscripcionRepository.findByEstudiante(id_estudiante);
    }
    public List<Inscripcion> buscarPorRangoFechas(Date fechaInicio, Date fechaFin){
        return inscripcionRepository.findByRangoFechas(fechaInicio,fechaFin);
    }
    public Long inscripcionesPorCurso(Long idCurso){
        return inscripcionRepository.countByCurso(idCurso);
    }
}
