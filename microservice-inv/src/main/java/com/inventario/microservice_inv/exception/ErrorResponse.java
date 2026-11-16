package com.inventario.microservice_inv.exception;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        Map<String, String> detalles
) {
}
