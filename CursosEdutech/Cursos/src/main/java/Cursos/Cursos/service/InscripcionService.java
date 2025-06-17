package Cursos.Cursos.service;

import Cursos.Cursos.model.Inscripcion;
import Cursos.Cursos.repository.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class InscripcionService {
    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String usuariosUrl;

    public InscripcionService(InscripcionRepository inscripcionRepository, RestTemplate restTemplate) {
        this.inscripcionRepository = inscripcionRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${servicio.usuarios.url}")
    public void setUsuariosUrl(String usuariosUrl) {
        this.usuariosUrl = usuariosUrl;
    }


    public Inscripcion guardar(Inscripcion inscripcion) {
        String url = usuariosUrl + "/" + inscripcion.getId_estudiante();
        Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);

        if (usuario == null || !"Estudiante".equalsIgnoreCase((String) usuario.get("dtype"))) {
            throw new RuntimeException("El usuario no existe o no es un Estudiante");
        }

        return inscripcionRepository.save(inscripcion);
    }
    public Optional<Inscripcion> findById(Long id) {
        return inscripcionRepository.findById(id);
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
