package com.examen.model;

/**
 * Estados posibles del stock de un producto.
 */
public enum EstadoStock {
    SIN_STOCK,   // no queda ninguna unidad
    STOCK_BAJO,  // quedan pocas unidades (igual o menos que el minimo)
    DISPONIBLE   // hay stock suficiente
}