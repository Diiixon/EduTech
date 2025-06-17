package com.EduTech.cl.pago_service.service;

import com.EduTech.cl.pago_service.model.CarroCompra;
import com.EduTech.cl.pago_service.model.CursoCarro;
import com.EduTech.cl.pago_service.model.Pago;
import com.EduTech.cl.pago_service.repository.CarroCompraRepository;
import com.EduTech.cl.pago_service.repository.PagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CarroCompraServiceTest {

    @Mock
    private CarroCompraRepository carroCompraRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarroCompraService carroCompraService;

    private CarroCompra carro;

    @BeforeEach
    void setUp() {
        carro = new CarroCompra();
        carro.setIdCarroCompra(Long.valueOf(1));
        carro.setIdEstudiante(Long.valueOf(10));
        carro.setPagado(false);
        carro.setCursos(new ArrayList<>());
    }

    @Test
    void testObtenerCarroExistente() {
        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(10)))
                .thenReturn(Optional.of(carro));

        CarroCompra resultado = carroCompraService.obtenerCarro(Long.valueOf(10));

        assertNotNull(resultado);
        assertEquals(Long.valueOf(10), resultado.getIdEstudiante());
    }

    @Test
    void testAgregarCurso() {
        Map<String, Object> usuarioMock = new HashMap<>();
        usuarioMock.put("dtype", "Estudiante");

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(usuarioMock);
        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(10)))
                .thenReturn(Optional.of(carro));
        when(carroCompraRepository.save(any(CarroCompra.class))).thenReturn(carro);

        CarroCompra resultado = carroCompraService.agregarCurso(
                Long.valueOf(10), Long.valueOf(100), "Curso Test", 50.0);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCursos().size());
        assertEquals("Curso Test", resultado.getCursos().get(0).getNombre());
    }

    @Test
    void testQuitarCurso() {
        CursoCarro curso = new CursoCarro(Long.valueOf(100), "Curso Test", 50.0);
        carro.getCursos().add(curso);

        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(10)))
                .thenReturn(Optional.of(carro));
        when(carroCompraRepository.save(any(CarroCompra.class))).thenReturn(carro);

        CarroCompra resultado = carroCompraService.quitarCurso(Long.valueOf(10), Long.valueOf(100));

        assertTrue(resultado.getCursos().isEmpty());
    }

    @Test
    void testPagarCarro() {
        CursoCarro curso = new CursoCarro(Long.valueOf(100), "Curso Test", 75.0);
        carro.getCursos().add(curso);

        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(10)))
                .thenReturn(Optional.of(carro));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));
        when(carroCompraRepository.save(any(CarroCompra.class))).thenReturn(carro);

        Pago pago = carroCompraService.pagarCarro(Long.valueOf(10));

        assertNotNull(pago);
        assertEquals(75.0, pago.getMonto());
        assertTrue(carro.isPagado());
    }

}
