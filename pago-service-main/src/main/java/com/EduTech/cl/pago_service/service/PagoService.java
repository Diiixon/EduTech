package com.EduTech.cl.pago_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.model.CursoCarro;
import com.EduTech.cl.pago_service.repository.CarroCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> findById(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void deleteById(Long id) {
        pagoRepository.deleteById(id);
    }

    @Autowired
    private CarroCompraRepository carroCompraRepository;

    public Pago pagarCarroCompra(Long idEstudiante, String metodoPago) {
        // Buscar el carro de compra del estudiante
        Optional<CarroCompra> optionalCarro = carroCompraRepository.findByidEstudianteAndPagadoFalse(idEstudiante);
        if (optionalCarro.isEmpty()) {
            throw new IllegalArgumentException("Carro no encontrado para el estudiante");
        }

        CarroCompra carro = optionalCarro.get();

        if (carro.getCursos().isEmpty()) {
            throw new IllegalArgumentException("El carro está vacío");
        }

        // Calcular monto total
        double total = carro.getCursos().stream()
                .mapToDouble(CursoCarro::getPrecio)
                .sum();

        // Crear el pago
        Pago pago = new Pago();
        pago.setIdEstudiante(idEstudiante);
        pago.setFecha(LocalDateTime.now());
        pago.setMonto(total);
        pago.setMetodoPago(metodoPago);

        Pago pagoGuardado = pagoRepository.save(pago);

        // Vaciar el carro (opcional pero recomendable)
        carro.getCursos().clear();
        carroCompraRepository.save(carro);

        return pagoGuardado;
    }


}
