package cl.duocuc.edutech.service;

import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Usuario buscarPorRUT(Integer rut){

        return usuarioRepository.findByRut(rut);
    }

    public Usuario findByCorreo(String correo){

        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario findById(Long id) {

        return usuarioRepository.findById(id).orElse(null);
    }


}
