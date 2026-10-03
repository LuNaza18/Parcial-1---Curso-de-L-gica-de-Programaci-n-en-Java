package com.examen.repository;

import com.examen.model.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Devuelve los productos que hay que reponer, pueden estar stock bajo o sin stock
    @Query("SELECT p FROM Producto p WHERE p.cantidad <= p.stockMinimo")
    List<Producto> buscarConStockBajo();
}