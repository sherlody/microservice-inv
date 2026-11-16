package com.inventario.microservice_inv.exception;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Errores de validación (@NotBlank, @Positive, etc.) -> HTTP 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> erroresCampos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erroresCampos.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse respuesta = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                null,
                request.getRequestURI(),
                erroresCampos
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 2. JSON mal formado o tipos de dato incorrectos -> HTTP 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        // Solo la causa raíz (mucho más limpia que el mensaje completo de Jackson)
        String causaRaiz = ex.getMostSpecificCause().getMessage();

        ErrorResponse respuesta = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Cuerpo JSON inválido o mal formado",
                causaRaiz,
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 3. Recursos inexistentes -> HTTP 404
    @ExceptionHandler(ResourceNotAvailable.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            ResourceNotAvailable ex,
            HttpServletRequest request) {

        ErrorResponse respuesta = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // 4. Cualquier otro error no controlado -> HTTP 500 (con log, sin filtrar stacktrace al cliente)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGenerico(
            Exception ex,
            HttpServletRequest request) {

        log.error("Error no controlado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse respuesta = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                null,
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}