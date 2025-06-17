package com.EduTech.cl.pago_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private CarroCompraRepository carroCompraRepository;

    @InjectMocks
    private PagoService pagoService;

    private CarroCompra carro;
    private Pago pago;
    private CursoCarro curso;

    @BeforeEach
    void setUp() {
        curso = new CursoCarro(Long.valueOf(10), "Curso Test", 20.0);

        carro = new CarroCompra();
        carro.setIdCarroCompra(Long.valueOf(1));
        carro.setIdEstudiante(Long.valueOf(100));
        carro.setPagado(false);
        carro.getCursos().add(curso);

        pago = new Pago();
        pago.setIdPago(Long.valueOf(1));
        pago.setIdEstudiante(Long.valueOf(100));
        pago.setMonto(20.0);
        pago.setMetodoPago("tarjeta");
    }

    @Test
    void testFindAll() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago));

        List<Pago> lista = pagoService.findAll();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(Long.valueOf(100), lista.get(0).getIdEstudiante());
    }

    @Test
    void testFindById_Existente() {
        when(pagoRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(pago));

        Optional<Pago> resultado = pagoService.findById(Long.valueOf(1));

        assertTrue(resultado.isPresent());
        assertEquals("tarjeta", resultado.get().getMetodoPago());
    }

    @Test
    void testFindById_NoExistente() {
        when(pagoRepository.findById(Long.valueOf(999))).thenReturn(Optional.empty());

        Optional<Pago> resultado = pagoService.findById(Long.valueOf(999));

        assertFalse(resultado.isPresent());
    }

    @Test
    void testSave() {
        when(pagoRepository.save(pago)).thenReturn(pago);

        Pago guardado = pagoService.save(pago);

        assertNotNull(guardado);
        assertEquals(Long.valueOf(100), guardado.getIdEstudiante());
    }

    @Test
    void testDeleteById() {
        doNothing().when(pagoRepository).deleteById(Long.valueOf(1));

        assertDoesNotThrow(() -> pagoService.deleteById(Long.valueOf(1)));
        verify(pagoRepository, times(1)).deleteById(Long.valueOf(1));
    }

    @Test
    void testPagarCarroCompra_Exitoso() {
        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(100)))
                .thenReturn(Optional.of(carro));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Pago resultado = pagoService.pagarCarroCompra(Long.valueOf(100), "tarjeta");

        assertNotNull(resultado);
        assertEquals(20.0, resultado.getMonto());
        assertEquals("tarjeta", resultado.getMetodoPago());
    }

    @Test
    void testPagarCarroCompra_CarroNoEncontrado() {
        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(100)))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                pagoService.pagarCarroCompra(Long.valueOf(100), "tarjeta"));

        assertEquals("Carro no encontrado para el estudiante", ex.getMessage());
    }

    @Test
    void testPagarCarroCompra_CarroVacio() {
        carro.getCursos().clear();
        when(carroCompraRepository.findByidEstudianteAndPagadoFalse(Long.valueOf(100)))
                .thenReturn(Optional.of(carro));

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                pagoService.pagarCarroCompra(Long.valueOf(100), "tarjeta"));

        assertEquals("El carro está vacío", ex.getMessage());
    }

}
