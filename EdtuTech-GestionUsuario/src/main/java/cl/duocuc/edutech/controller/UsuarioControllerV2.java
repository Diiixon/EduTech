package cl.duocuc.edutech.controller;

import cl.duocuc.edutech.assembler.UsuarioModelAssembler;
import cl.duocuc.edutech.dto.LoginResponseDTO;
import cl.duocuc.edutech.dto.RegistroUsuarioDTO;
import cl.duocuc.edutech.model.Empleado;
import cl.duocuc.edutech.model.Estudiante;
import cl.duocuc.edutech.model.Rol;
import cl.duocuc.edutech.model.Usuario;
import cl.duocuc.edutech.repository.RolRepository;
import cl.duocuc.edutech.service.LoginService;
import cl.duocuc.edutech.service.UsuarioService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.transaction.Transactional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {

    private final LoginService loginService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioModelAssembler assembler;

    @Autowired
    public UsuarioControllerV2(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Iniciar sesión", description = "Permite al usuario iniciar sesión con correo y clave")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor") })
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Parameter(description = "Correo del usuario", example = "usuario@duocuc.cl") @RequestParam String correo,
            @Parameter(description = "Clave del usuario", example = "123456") @RequestParam String clave) {
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

    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista con todos los usuarios registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll()
                .stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(usuarios,
                        linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel()));
    }

    @Operation(summary = "Registrar nuevo usuario", description = "Permite registar un nuevo usuario, el cual puede ser un estudiante o un empleado."
            + "Para los estudiantes se requiere fecha de registro y para los empleado se requiere el Id del rol.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos incorrectos o no soportados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor") })
    @Transactional
    @PostMapping("/registro")
    public ResponseEntity<EntityModel<Usuario>> registrarUsuario(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para el registro del usuario (estudiante o empleado)", required = true, content = @Content(schema = @Schema(implementation = RegistroUsuarioDTO.class))) RegistroUsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioGuardado;

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

                usuarioGuardado = usuarioService.save(estudiante);

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

                usuarioGuardado = usuarioService.save(empleado);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assembler.toModel(usuarioGuardado));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar Usuario", description = "Actualiza los datos personales de un usuario existente usando su RUT"
            + "No se permite cambiar el RUT ni el tipo de usuario (empleado o estudiante).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizdo correctamente", content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud") })
    @PutMapping("/{rut}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(
            @Parameter(description = "RUT del usuario que desea actualizar", example = "12345678") @PathVariable int rut,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del usuario", required = true, content = @Content(schema = @Schema(implementation = Usuario.class))) @RequestBody Usuario usuario) {
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

            Usuario actualizado = usuarioService.save(usuarioExistente);

            return ResponseEntity.ok(assembler.toModel(actualizado));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Recuperar clave de usuario", description = "Recupera la clave de un usuario registrado a partir de su correo"
            + "Este método devuelve la clave directamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clave recuperada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error del servidor")
    })
    @GetMapping("/recuperar_clave/{correo}")
    public ResponseEntity<String> recuperarClave(
            @Parameter(description = "Correo del usuario", example = "usuario@duocuc.cl") @PathVariable String correo) {
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

    @Operation(summary = "Obtener usuario por ID", description = "Retorna la información detallada de un usuario según su ID"
            + "La respuesta incluye los datos generales y dependiendo del tipo de usuario, información adicional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(
            @Parameter(description = "Id del usuario", example = "1") @PathVariable Long id) {
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

        return ResponseEntity.ok(assembler.toModel(usuario));
    }

}
