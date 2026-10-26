package com.productos.controller;

import com.productos.dto.ProductoResponse;
import com.productos.exception.ProductoNoEncontradoException;
import com.productos.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de los endpoints con el servicio simulado.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductoService service;

    @Test
    void getProductosDevuelveLaLista() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new ProductoResponse(1L, "Teclado", new BigDecimal("120000.00"), 5)));

        mvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Teclado"))
                .andExpect(jsonPath("$[0].stock").value(5));
    }

    @Test
    void postProductosGuardaElProducto() throws Exception {
        when(service.guardar(any())).thenReturn(
                new ProductoResponse(1L, "Mouse", new BigDecimal("45000"), 10));

        mvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "Mouse", "precio": 45000, "stock": 10}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Mouse"));
    }

    @Test
    void postProductosConDatosInvalidosDevuelve400() throws Exception {
        mvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre": "", "precio": -1, "stock": null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles.length()").value(3));
    }

    @Test
    void postProductosIdEliminaElProducto() throws Exception {
        mvc.perform(post("/productos/7"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(7L);
    }

    @Test
    void eliminarProductoInexistenteDevuelve404() throws Exception {
        doThrow(new ProductoNoEncontradoException(7L)).when(service).eliminar(7L);

        mvc.perform(post("/productos/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("No existe un producto con id 7"));
    }
}
