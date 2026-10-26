package com.productos.exception;

/**
 * Se lanza cuando se busca un producto con un id que no existe.
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con id " + id);
    }
}
