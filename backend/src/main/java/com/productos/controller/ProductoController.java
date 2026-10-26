package com.productos.controller;

import com.productos.dto.ProductoRequest;
import com.productos.dto.ProductoResponse;
import com.productos.service.ProductoService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints REST para gestionar productos.
 */
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    /** Lista todos los productos. */
    @GetMapping
    public List<ProductoResponse> listar() {
        return service.listar();
    }

    /** Crea el producto si no trae id, o lo actualiza si lo trae. */
    @PostMapping
    public ProductoResponse guardar(@Valid @RequestBody ProductoRequest request) {
        return service.guardar(request);
    }

    /** Elimina el producto. Responde 204 si se elimino. */
    @PostMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
