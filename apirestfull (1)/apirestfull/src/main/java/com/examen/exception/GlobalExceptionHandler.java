package com.examen.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice  // Aplica esto a todos los controladores y devuelve JSON
public class GlobalExceptionHandler {

    // Se ejecuta cuando alguna parte del codigo lanza RecursoNoEncontradoException
    // (por ejemplo, al buscar un producto con un id que no existe)
    // Responde 404 (Not Found) con el mensaje de la excepcion
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        // LinkedHashMap mantiene el orden en que se agregan los datos al JSON
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());           // fecha y hora del error
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());     // codigo numerico: 404
        cuerpo.put("error", ex.getMessage());                   // mensaje que armo quien lanzo la excepcion
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }

    // Se ejecuta cuando los datos recibidos no cumplen las validaciones de la entidad
    // (por ejemplo, nombre vacio o cantidad negativa, marcados con @Valid en el controlador)
    // Responde 400 (Bad Request) con el detalle de cada campo que fallo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        // Mapa con el nombre del campo y su mensaje de error: {"nombre": "El nombre es obligatorio"}
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        // Cuerpo de la respuesta, con el mismo formato que el error 404 mas la lista de detalles
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());   // codigo numerico: 400
        cuerpo.put("error", "Datos invalidos");
        cuerpo.put("detalles", errores);                        // campos que fallaron y por que
        return ResponseEntity.badRequest().body(cuerpo);
    }
}