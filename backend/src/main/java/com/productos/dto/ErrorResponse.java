package com.productos.dto;

import java.util.List;

/**
 * Cuerpo de las respuestas de error.
 *
 * @param status   codigo HTTP
 * @param mensaje  descripcion general del error
 * @param detalles errores por campo, si los hay
 */
public record ErrorResponse(int status, String mensaje, List<String> detalles) {

    public ErrorResponse(int status, String mensaje) {
        this(status, mensaje, List.of());
    }
}
