package cl.duocuc.edutech.service;

import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){

        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario){

        return usuarioRepository.save(usuario);
    }

    public void eliminarPorRUT(int rut){

        usuarioRepository.deleteByRut(rut);
    }

    public Usuario buscarPorRUT(Integer rut){

        return usuarioRepository.findByRut(rut);
    }

    public Usuario findByCorreo(String correo){

        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario iniciarSesion(String correo, String clave){
        return usuarioRepository.iniciarSesion(correo, clave);
    }

    public Usuario findById(Long id) {
    return usuarioRepository.findById(id).orElse(null);
    }


    // Servicio en EduTech
@Service
public class ReporteService {

    private final RestTemplate restTemplate;

    public ReporteService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Long contarTotalEstudiantes() {
        return restTemplate.getForObject("http://gestion-usuarios/estudiantes/total", Long.class);
    }
}

}
