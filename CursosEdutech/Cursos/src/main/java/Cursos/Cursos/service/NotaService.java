package Cursos.Cursos.service;

import Cursos.Cursos.model.Nota;
import Cursos.Cursos.repository.NotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NotaService {
    @Autowired
    private NotaRepository notaRepository;
    public List<Nota> obtenerNotasPorInscripcion(Long idInscripcion){
        return notaRepository.findByInscripcion(idInscripcion);
    }
}
