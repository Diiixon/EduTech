package cl.duocuc.edutech;

import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.UsuarioRepository;
import cl.duocuc.edutech.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll(){

        Usuario usuario = new Usuario(1,12345678,'K',"Carlos","Pérez","correo@duocuc.cl",987654321,"password");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<Usuario> usuarios = usuarioService.findAll();

        assertEquals(1, usuarios.size());
        verify(usuarioRepository, times(1)).findAll();

    }

    @Test
    void testSave(){
        Usuario usuario = new Usuario(1,123455678,'K',"Carlos","Pérez","correo@duocuc.cl",987654321,"password");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.save(usuario);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testBuscarPorRut(){
        Integer rutBuscado = 12345678;
        Usuario usuario = new Usuario();
        usuario.setRut(rutBuscado);

        when(usuarioRepository.findByRut(rutBuscado)).thenReturn(usuario);

        Usuario resultado = usuarioService.buscarPorRUT(rutBuscado);

        assertNotNull(resultado);
        assertEquals(rutBuscado, resultado.getRut());
        verify(usuarioRepository, times(1)).findByRut(rutBuscado);
    }

    @Test
    void testFindByCorreo(){
        String correo = "correo@duocuc.cl";

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);

        when(usuarioRepository.findByCorreo(correo)).thenReturn(usuario);

        Usuario resultado = usuarioService.findByCorreo(correo);

        assertNotNull(resultado);
        assertEquals(correo, resultado.getCorreo());
        verify(usuarioRepository, times(1)).findByCorreo(correo);
    }

    @Test
    void testFindById(){
        Long idBuscado = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(idBuscado.intValue());

        when(usuarioRepository.findById(idBuscado)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado.intValue(), resultado.getId());
        verify(usuarioRepository, times(1)).findById(idBuscado);
    }
}
