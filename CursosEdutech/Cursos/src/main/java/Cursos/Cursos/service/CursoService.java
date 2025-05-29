package Cursos.Cursos.service;

import Cursos.Cursos.model.Curso;
import Cursos.Cursos.repository.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CursoService {
    @Autowired
    private CursoRepository cursoRepository;
    public Curso guardar(Curso curso){
        return cursoRepository.save(curso);
    }
    public Optional<Curso> obtenerPorId(Long id_curso){
        return cursoRepository.findById((id_curso));
    }
    public List<Curso> obtenerTodos(){
        return cursoRepository.findAll();
    }
    public void eliminar(Long id_curso) {
        cursoRepository.deleteById(id_curso);
    }
    public List<Curso> buscarPorPrecio(Integer precioMin, Integer precioMax){
        return cursoRepository.findByRangoPrecio(precioMin, precioMax);
    }
    public List<Curso> buscarPorCategoria(Long id_categoria){
        return cursoRepository.findByCategoriaId((id_categoria));
    }
    public List<Curso> buscarCursosPorPalabra(String palabra){
        return cursoRepository.findCursosByNombreContaining(palabra);
    }
}
