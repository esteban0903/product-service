package com.productos.service;

import com.productos.dto.ProductoRequest;
import com.productos.dto.ProductoResponse;
import com.productos.entity.Producto;
import com.productos.exception.ProductoNoEncontradoException;
import com.productos.repository.ProductoRepository;
import com.productos.service.impl.ProductoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias del servicio con el repositorio simulado.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    private ProductoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductoServiceImpl(repository);
    }

    @Test
    void listarDevuelveTodosLosProductos() {
        when(repository.findAll(any(Sort.class)))
                .thenReturn(List.of(producto(1L, "Teclado", "120000.00", 5)));

        List<ProductoResponse> resultado = service.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nombre()).isEqualTo("Teclado");
    }

    @Test
    void guardarSinIdCreaUnProductoNuevo() {
        ProductoRequest request = new ProductoRequest(null, "  Mouse ", new BigDecimal("45000"), 10);
        when(repository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponse resultado = service.guardar(request);

        assertThat(resultado.nombre()).isEqualTo("Mouse");
        assertThat(resultado.stock()).isEqualTo(10);
        verify(repository, never()).findById(any());
    }

    @Test
    void guardarConIdActualizaElProductoExistente() {
        Producto existente = producto(3L, "Monitor", "800000.00", 2);
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        ProductoResponse resultado = service.guardar(
                new ProductoRequest(3L, "Monitor 27", new BigDecimal("950000.00"), 4));

        assertThat(resultado.id()).isEqualTo(3L);
        assertThat(resultado.nombre()).isEqualTo("Monitor 27");
        assertThat(resultado.precio()).isEqualByComparingTo("950000.00");
        assertThat(resultado.stock()).isEqualTo(4);
    }

    @Test
    void guardarConIdInexistenteLanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.guardar(
                new ProductoRequest(99L, "X", BigDecimal.ONE, 1)))
                .isInstanceOf(ProductoNoEncontradoException.class);
    }

    @Test
    void eliminarBorraCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminarLanzaExcepcionCuandoNoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(1L))
                .isInstanceOf(ProductoNoEncontradoException.class);
        verify(repository, never()).deleteById(any());
    }

    private Producto producto(Long id, String nombre, String precio, int stock) {
        Producto producto = new Producto(nombre, new BigDecimal(precio), stock);
        ReflectionTestUtils.setField(producto, "id", id);
        return producto;
    }
}
