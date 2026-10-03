package com.examen.service;

import com.examen.model.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de la logica de negocio. El controlador depende de esta
 * interfaz y no de la implementacion.
 */
public interface ProductoServicio {

    List<Producto> obtenerProductos();

    Optional<Producto> obtenerPorId(Long id);

    Optional<Producto> crearProducto(Producto producto);

    Optional<Producto> actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);

    // Productos que hay que reponer: stock bajo o sin stock
    List<Producto> obtenerStockBajo();
}