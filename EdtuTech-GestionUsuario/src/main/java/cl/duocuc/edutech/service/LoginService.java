package cl.duocuc.edutech.service;

import cl.duocuc.edutech.dto.LoginResponseDTO;
import cl.duocuc.edutech.model.Empleado;
import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.EmpleadoRepository;
import cl.duocuc.edutech.repository.EstudianteRepository;
import cl.duocuc.edutech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public LoginService(UsuarioRepository usuarioRepository, EstudianteRepository estudianteRepository, EmpleadoRepository empleadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.estudianteRepository = estudianteRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public LoginResponseDTO iniciarSesion(String correo, String clave){
        Usuario usuario = usuarioRepository.iniciarSesion(correo, clave);

        if(usuario == null){
            throw new RuntimeException("Credenciales incorrectas");
        }

        boolean esEstudiante = estudianteRepository.findByRut(usuario.getRut()).isPresent();
        Optional<Empleado> empleadoOpt = Optional.ofNullable(empleadoRepository.findByRut(usuario.getRut()));

        String rol;
        if(esEstudiante){
            rol = "ESTUDIANTE";
        }else if(empleadoOpt.isPresent()){
            rol = empleadoOpt.map(empleado -> 
                Optional.ofNullable(empleado.getRolEmpleado())
                       .map(rolEmpleado -> rolEmpleado.getNombre())
                       .orElse("Empleado sin Rol"))
                .orElse("Empleado sin Rol");
        }else{
            rol = "DESCONOCIDO";
        }

        if (usuario.getNombres() == null || usuario.getApellidos() == null) {
            throw new IllegalStateException("Nombres o apellidos no pueden ser null");
        }

        return new LoginResponseDTO(
            usuario.getRut(),
            usuario.getNombres(),
            usuario.getApellidos(),
            usuario.getCorreo(),
            rol
        );
    }
}