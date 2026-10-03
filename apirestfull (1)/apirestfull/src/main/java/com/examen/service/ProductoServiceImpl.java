package com.examen.service;

import com.examen.exception.RecursoNoEncontradoException;
import com.examen.model.Producto;
import com.examen.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementacion de la logica de negocio del inventario.
 */
@Service
public class ProductoServiceImpl implements ProductoServicio {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> crearProducto(Producto producto) {
        Producto productoParaGuardar = new Producto();
        productoParaGuardar.setNombre(producto.getNombre());
        productoParaGuardar.setCategoria(producto.getCategoria());
        productoParaGuardar.setCantidad(producto.getCantidad());
        productoParaGuardar.setStockMinimo(producto.getStockMinimo());
        productoParaGuardar.setPrecio(producto.getPrecio());
        return Optional.of(productoRepository.save(productoParaGuardar));
    }

    @Override
    public Optional<Producto> actualizarProducto(Long id, Producto producto) {
        Optional<Producto> productoViejo = productoRepository.findById(id);
        if (productoViejo.isPresent()) {
            Producto productoParaActualizar = productoViejo.get();
            productoParaActualizar.setNombre(producto.getNombre());
            productoParaActualizar.setCategoria(producto.getCategoria());
            productoParaActualizar.setCantidad(producto.getCantidad());
            productoParaActualizar.setStockMinimo(producto.getStockMinimo());
            productoParaActualizar.setPrecio(producto.getPrecio());
            return Optional.of(productoRepository.save(productoParaActualizar));
        }
        return Optional.empty();
    }

    @Override
    public void eliminarProducto(Long id) {
        // deleteById no avisa si el id no existe, asi que lo verificamos nosotros
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un producto con id " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> obtenerStockBajo() {
        return productoRepository.buscarConStockBajo();
    }
}