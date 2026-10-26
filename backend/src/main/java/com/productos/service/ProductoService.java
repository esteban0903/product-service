package com.productos.service;

import com.productos.dto.ProductoRequest;
import com.productos.dto.ProductoResponse;

import java.util.List;

/**
 * Operaciones disponibles sobre los productos.
 */
public interface ProductoService {

    /**
     * Devuelve todos los productos ordenados por id.
     */
    List<ProductoResponse> listar();

    /**
     * Crea el producto si no trae id, o lo actualiza si lo trae.
     *
     * @throws com.productos.exception.ProductoNoEncontradoException si el id no existe
     */
    ProductoResponse guardar(ProductoRequest request);

    /**
     * Elimina el producto con el id dado.
     *
     * @throws com.productos.exception.ProductoNoEncontradoException si el id no existe
     */
    void eliminar(Long id);
}
