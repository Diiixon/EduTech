package cl.duocuc.edutech.service;

import cl.duocuc.edutech.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public Integer contarTotalEstudiantes() {

        return estudianteRepository.contarTotalEstudiantes();
    }

}
