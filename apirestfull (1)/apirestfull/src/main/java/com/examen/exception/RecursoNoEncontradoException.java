package com.examen.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    //Devuelve un mensaje cuando no encuentra un recurso (por ejemplo, un producto con un id que no existe)
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}