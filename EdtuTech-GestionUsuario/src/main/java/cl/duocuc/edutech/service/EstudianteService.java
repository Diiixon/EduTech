package cl.duocuc.edutech.service;

import cl.duocuc.edutech.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Object> explorarCatalogo;

    public Integer contarTotalEstudiantes() {
        return estudianteRepository.contarTotalEstudiantes();
    }

}
