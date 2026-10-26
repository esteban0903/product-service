package com.productos.dto;

import com.productos.entity.Producto;

import java.math.BigDecimal;

/**
 * Producto que se devuelve al cliente.
 */
public record ProductoResponse(Long id, String nombre, BigDecimal precio, Integer stock) {

    /**
     * Convierte la entidad en la respuesta de la API.
     */
    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
