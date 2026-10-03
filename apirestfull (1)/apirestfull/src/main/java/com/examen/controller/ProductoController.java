package com.examen.controller;

import com.examen.exception.RecursoNoEncontradoException;
import com.examen.model.Producto;
import com.examen.service.ProductoServicio;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST de productos.
 * Recibe las solicitudes HTTP, se las pasa al servicio y devuelve la respuesta.
 */
@RestController                    // La clase funciona con solicitudes HTTP y devuelve JSON
@RequestMapping("/api/productos")  //Esta es la ruta base para todos los endpoints de esta clase
public class ProductoController {

    // El servicio que contiene la logica de negocio (se usa la interfaz, no la implementacion)
    private final ProductoServicio productoServicio;

    // I Spring entrega el servicio automaticamente
    public ProductoController(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    // GET /api/productos:  devuelve la lista de todos los productos
    @GetMapping
    public List<Producto> obtenerProductos() {
        return productoServicio.obtenerProductos();
    }

    // GET /api/productos/stock-bajo: devuelve solo los productos que hay que reponer
    // (los que estan sin stock o con stock igual o menor al minimo)
    @GetMapping("/stock-bajo")
    public List<Producto> obtenerStockBajo() {
        return productoServicio.obtenerStockBajo();
    }

    // GET /api/productos/{id}: devuelve un producto segun su id
    // Si no existe, lanza la excepcion personalizada (el manejador de errores responde 404)
    @GetMapping("/{id}")
    public Producto obtenerProducto(@PathVariable Long id) { // @PathVariable toma el id de la URL
        return productoServicio.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + id));
    }

    // POST /api/productos: crea un producto nuevo
    // @RequestBody convierte el JSON recibido en un objeto Producto
    // @Valid comprueba las validaciones de la entidad (nombre obligatorio, cantidad no negativa, etc.)
    // Si los datos no son validos, Spring responde 400 sin llegar a ejecutar este metodo
    @PostMapping
    public ResponseEntity<Optional<Producto>> crearProducto(@Valid @RequestBody Producto producto) {
        Optional<Producto> nuevoProducto = productoServicio.crearProducto(producto);
        // Responde 201 (Created) con el producto guardado en el cuerpo
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // PUT /api/productos/{id}: actualiza un producto existente
    // Si el id no existe, lanza la excepcion personalizada (404)
    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoServicio.actualizarProducto(id, producto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto con id " + id));
    }

    // DELETE /api/productos/{id}: elimina un producto
    // Responde 204 (No Content): se elimino correctamente y no hay nada que devolver
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoServicio.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}