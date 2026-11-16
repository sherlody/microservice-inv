package com.inventario.microservice_inv.dto;

import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuestas ante errores HTTP")
public record ErrorResponse(
    @Schema(example = "2026-11-16T08:15:30.120")
        String timestamp,

        @Schema(example = "400")
        int status,

        @Schema(example = "Error de validación")
        String error,

        @Schema(example = "No se encontró el producto con ID: 999")
        String mensaje,

        @Schema(example = "/api/productos")
        String path,

        @Schema(example = "{\"nombre\": \"El nombre del producto es obligatorio\"}")
        Map<String, String> detalles
) {
}
