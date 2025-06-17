package cl.duocuc.edutech;

import cl.duocuc.edutech.dto.LoginResponseDTO;
import cl.duocuc.edutech.model.Empleado;
import cl.duocuc.edutech.model.Rol;
import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.EmpleadoRepository;
import cl.duocuc.edutech.repository.EstudianteRepository;
import cl.duocuc.edutech.repository.UsuarioRepository;
import cl.duocuc.edutech.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIniciarSesionEstudiante() {
        String correo = "estudiante@duocuc.cl";
        String clave = "password";

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setClave(clave);
        usuario.setNombres("Carlos");
        usuario.setApellidos("Pérez");

        when(usuarioRepository.iniciarSesion(correo, clave)).thenReturn(usuario);

        LoginResponseDTO response = loginService.iniciarSesion(correo, clave);

        assertEquals(usuario.getCorreo(), response.getCorreo());
        verify(usuarioRepository, times(1)).iniciarSesion(correo, clave);
    }

    @Test
    void testIniciarSesionEmpleado() {
        String correo = "empleado@duocuc.cl";
        String clave = "password";

        Usuario usuario = new Usuario();
        usuario.setCorreo(correo);
        usuario.setClave(clave);
        usuario.setNombres("Carlos");
        usuario.setApellidos("Pérez");
        usuario.setRut(12345678);

        Empleado empleado = new Empleado();
        empleado.setRut(usuario.getRut());
        Rol rolEmpleado = new Rol();
        rolEmpleado.setNombre("PROFESOR");
        empleado.setRolEmpleado(rolEmpleado);

        when(usuarioRepository.iniciarSesion(correo, clave)).thenReturn(usuario);
        when(empleadoRepository.findByRut(usuario.getRut())).thenReturn(empleado);


        LoginResponseDTO response = loginService.iniciarSesion(correo, clave);

        assertEquals(usuario.getCorreo(), response.getCorreo());
        assertEquals("PROFESOR", response.getRol());

        verify(usuarioRepository, times(1)).iniciarSesion(correo, clave);

    }
}
