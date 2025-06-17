package com.EduTech.cl.pago_service.service;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.model.CursoCarro;
import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.repository.CarroCompraRepository;
import com.EduTech.cl.pago_service.repository.PagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CarroCompraService {

    @Autowired
    private CarroCompraRepository carroCompraRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${servicio.usuarios.url}")
    private String usuarioServiceUrl;

    @Value("${servicio.cursos.url}")
    private String cursoServiceUrl;

    public CarroCompra obtenerCarro(Long idEstudiante) {
        return carroCompraRepository.findByidEstudianteAndPagadoFalse(idEstudiante)
                .orElseGet(() -> {
                    if (!validarEstudiante(idEstudiante)) {
                        throw new RuntimeException("El usuario no existe o no es un Estudiante");
                    }
                    CarroCompra nuevo = new CarroCompra();
                    nuevo.setIdEstudiante(idEstudiante);
                    nuevo.setPagado(false);
                    return carroCompraRepository.save(nuevo);
                });
    }

    public CarroCompra agregarCurso(Long idEstudiante, Long idCurso, String nombre, double precio) {
        if (!validarEstudiante(idEstudiante)) {
            throw new RuntimeException("El usuario no existe o no es un Estudiante");
        }

        CarroCompra carro = obtenerCarro(idEstudiante);

        // Verificamos si el curso ya está en el carro para evitar duplicados
        boolean yaAgregado = carro.getCursos().stream()
                .anyMatch(c -> c.getIdCurso().equals(idCurso));
        if (yaAgregado) {
            throw new RuntimeException("El curso ya fue agregado al carro");
        }

        CursoCarro curso = new CursoCarro();
        curso.setIdCurso(idCurso);
        curso.setNombre(nombre);
        curso.setPrecio(precio);

        carro.getCursos().add(curso);
        return carroCompraRepository.save(carro);
    }


    public CarroCompra quitarCurso(Long idEstudiante, Long idCurso) {
        CarroCompra carro = obtenerCarro(idEstudiante);
        carro.getCursos().removeIf(c -> c.getIdCurso().equals(idCurso));
        return carroCompraRepository.save(carro);
    }

    @Transactional
    public Pago pagarCarro(Long idEstudiante) {
        CarroCompra carro = obtenerCarro(idEstudiante);

        if (carro.isPagado()) {
            throw new RuntimeException("El carro ya fue pagado");
        }

        if (carro.getCursos().isEmpty()) {
            throw new RuntimeException("El carro está vacío");
        }

        double montoTotal = carro.getCursos().stream()
                .mapToDouble(CursoCarro::getPrecio)
                .sum();

        Pago pago = new Pago();
        pago.setIdEstudiante(idEstudiante);
        pago.setMonto(montoTotal);
        pago.setMetodoPago("tarjeta"); // puedes hacerlo dinámico si deseas
        pago.setFecha(LocalDateTime.now());
        pago.setIdPago(carro.getIdCarroCompra());

        pagoRepository.save(pago);

        carro.setPagado(true);
        carroCompraRepository.save(carro);

        return pago;
    }

    private boolean validarEstudiante(Long idEstudiante) {
        try {
            String url = usuarioServiceUrl + "/" + idEstudiante;
            Map<String, Object> usuario = restTemplate.getForObject(url, Map.class);
            return usuario != null && "Estudiante".equalsIgnoreCase((String) usuario.get("dtype"));
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> obtenerCurso(Long idCurso) {
        try {
            String url = cursoServiceUrl + "/" + idCurso;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return null;
        }
    }
}
