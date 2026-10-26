package com.productos.service.impl;

import com.productos.dto.ProductoRequest;
import com.productos.dto.ProductoResponse;
import com.productos.entity.Producto;
import com.productos.exception.ProductoNoEncontradoException;
import com.productos.repository.ProductoRepository;
import com.productos.service.ProductoService;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementacion de {@link ProductoService} usando JPA.
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repository.findAll(Sort.by("id")).stream()
                .map(ProductoResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponse guardar(ProductoRequest request) {
        Producto producto = request.id() == null
                ? crear(request)
                : actualizar(request);
        return ProductoResponse.from(repository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductoNoEncontradoException(id);
        }
        repository.deleteById(id);
    }

    private Producto crear(ProductoRequest request) {
        return new Producto(request.nombre().trim(), request.precio(), request.stock());
    }

    private Producto actualizar(ProductoRequest request) {
        Producto producto = repository.findById(request.id())
                .orElseThrow(() -> new ProductoNoEncontradoException(request.id()));
        producto.actualizar(request.nombre().trim(), request.precio(), request.stock());
        return producto;
    }
}
