package cl.duocuc.edutech.controller;

import cl.duocuc.edutech.dto.LoginResponseDTO;
import cl.duocuc.edutech.dto.RegistroUsuarioDTO;
import cl.duocuc.edutech.model.Empleado;
import cl.duocuc.edutech.model.Estudiante;
import cl.duocuc.edutech.model.Rol;
import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.RolRepository;
import cl.duocuc.edutech.service.LoginService;
import cl.duocuc.edutech.service.UsuarioService;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final LoginService loginService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    public UsuarioController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    private UsuarioService usuarioService;

    
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String correo, @RequestParam String clave) {
        try {
            LoginResponseDTO respuesta = loginService.iniciarSesion(correo, clave);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("mensaje", "Inicio de sesión correcto, bienvenido");
            response.put("datos", respuesta);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(401).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("mensaje", "Ocurrió un error inesperado");
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Transactional
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegistroUsuarioDTO usuarioDTO) {
        try {
            if (usuarioDTO.getTipo().equalsIgnoreCase("estudiante")) {
                Estudiante estudiante = new Estudiante();
                estudiante.setRut(usuarioDTO.getRut());
                estudiante.setDv(usuarioDTO.getDv());
                estudiante.setNombres(usuarioDTO.getNombres());
                estudiante.setApellidos(usuarioDTO.getApellidos());
                estudiante.setCorreo(usuarioDTO.getCorreo());
                estudiante.setTelefono(usuarioDTO.getTelefono());
                estudiante.setClave(usuarioDTO.getClave());
                estudiante.setFechaRegistro(usuarioDTO.getFechaRegistro());

                Usuario usuarioGuardado = usuarioService.save(estudiante);
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);

            } else if (usuarioDTO.getTipo().equalsIgnoreCase("empleado")) {
                Empleado empleado = new Empleado();
                empleado.setRut(usuarioDTO.getRut());
                empleado.setDv(usuarioDTO.getDv());
                empleado.setNombres(usuarioDTO.getNombres());
                empleado.setApellidos(usuarioDTO.getApellidos());
                empleado.setCorreo(usuarioDTO.getCorreo());
                empleado.setTelefono(usuarioDTO.getTelefono());
                empleado.setClave(usuarioDTO.getClave());

                Rol rol = rolRepository.findById(usuarioDTO.getIdRol())
                        .orElseThrow(() -> new RuntimeException(
                                "No se encontró el rol con el ID: " + usuarioDTO.getIdRol()));
                empleado.setRolEmpleado(rol);

                Usuario usuarioGuardado = usuarioService.save(empleado);
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{rut}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable int rut, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioExistente = usuarioService.buscarPorRUT(rut);
            if (usuarioExistente == null) {
                return ResponseEntity.notFound().build();
            }

            usuarioExistente.setNombres(usuario.getNombres());
            usuarioExistente.setApellidos(usuario.getApellidos());
            usuarioExistente.setCorreo(usuario.getCorreo());
            usuarioExistente.setTelefono(usuario.getTelefono());
            usuarioExistente.setClave(usuario.getClave());

            usuarioService.save(usuarioExistente);
            return ResponseEntity.ok(usuarioExistente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/recuperar_clave/{correo}")
    public ResponseEntity<String> recuperarClave(@PathVariable String correo) {
        try {
            Usuario usuario = usuarioService.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            return ResponseEntity.ok("La clave es: " + usuario.getClave());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar la clave");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("rut", usuario.getRut());
        response.put("dv", usuario.getDv());
        response.put("nombres", usuario.getNombres());
        response.put("apellidos", usuario.getApellidos());
        response.put("correo", usuario.getCorreo());
        response.put("telefono", usuario.getTelefono());
        response.put("clave", usuario.getClave());

        if (usuario instanceof Estudiante) {
            response.put("dtype", "Estudiante");
            response.put("fechaRegistro", ((Estudiante) usuario).getFechaRegistro());
        } else if (usuario instanceof Empleado) {
            response.put("dtype", "Empleado");
            response.put("rol", ((Empleado) usuario).getRolEmpleado().getNombre());
        }

        return ResponseEntity.ok(response);
    }

}
