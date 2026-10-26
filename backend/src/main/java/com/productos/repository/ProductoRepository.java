package com.productos.repository;

import com.productos.entity.Producto;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Acceso a la base de datos para los productos.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
