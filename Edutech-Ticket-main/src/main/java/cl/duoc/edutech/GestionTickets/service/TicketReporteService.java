package cl.duoc.edutech.GestionTickets.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cl.duoc.edutech.GestionTickets.model.TicketReporte;
import cl.duoc.edutech.GestionTickets.repository.TicketReporteRepository;

@Service
public class TicketReporteService {

    @Autowired
    private TicketReporteRepository ticketReporteRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${usuarios.service.url}")
    private String urlUsuarios;

    @Value("${cursos.service.url}")
    private String urlCursos;

    public List<TicketReporte> findAll() {
        return ticketReporteRepository.findAll();
    }

    public TicketReporte save(String descripcion, Long idUsuario, Long idCurso) {
        if (!validarUsuarioExistente(idUsuario)) {
            throw new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe.");
        }

        if (idCurso != null && !validarCursoExistente(idCurso)) {
            throw new IllegalArgumentException("El curso con ID " + idCurso + " no existe.");
        }
        TicketReporte ticket = new TicketReporte(descripcion, idUsuario, idCurso);
        return ticketReporteRepository.save(ticket);
    }

    public Optional<TicketReporte> findById(Long id) {
        return ticketReporteRepository.findById(id);
    }

    /*public List<TicketReporte> findByIdUsuario(int idUsuario) {
        return ticketReporteRepository.findByIdUsuario(idUsuario);
    }*/

    public void deleteById(Long id) {
        ticketReporteRepository.deleteById(id);
    }

    public boolean responderTicket(Long id, String respuesta, Long idEmpleado) {
        if (!validarEmpleadoExistente(idEmpleado)){
            throw new IllegalArgumentException("El ID" + idEmpleado + " no pertenece a un empleado válido");
        }
    Optional<TicketReporte> optionalTicket = ticketReporteRepository.findById(id);
    if (optionalTicket.isPresent()) {
        TicketReporte ticket = optionalTicket.get();
        ticket.setRespuesta(respuesta);
        ticket.setIdEmpleado(idEmpleado);
        ticket.setEstado(true);
        ticketReporteRepository.save(ticket);
        return true;
    }
    return false;
}

    private boolean validarUsuarioExistente(Long idUsuario) {
        String url = urlUsuarios + "/" + idUsuario;
        try {
            restTemplate.getForEntity(url, Void.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Usuario con ID " + idUsuario + " no encontrado");
            return false;
        }
        catch (Exception e) {
            System.out.println("Error al comunicarse con el microservicio de Usuarios" + e.getMessage());
            return false;
        }
        
    }

        private boolean validarEmpleadoExistente(Long idEmpleado){
        String url = urlUsuarios + "/" + idEmpleado;
        try {
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            if (response == null) return false;

            String tipoUsuario = (String) response.get("dtype");
            String rol = (String) response.get("rol");

            return "Empleado".equalsIgnoreCase(tipoUsuario)
                    && "OPERADOR SOPORTE".equalsIgnoreCase(rol);

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Empleado con ID " + idEmpleado + " no encontrado");
            return false;
        } catch (Exception e) {
            System.out.println("Error al comunicarse con el microservicio de Usuarios: " + e.getMessage());
            return false;
        }
        }

    private boolean validarCursoExistente(Long idCurso){
        String url = urlCursos + "/" + idCurso;
        try {
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            return response != null;
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Curso con ID" + idCurso + " no encontrado");
            return false;
        } catch (Exception e) {
            System.out.println("Error al comunicarse con el microservicio de Cursos" + e.getMessage());
            return false;
        }
    }
}

