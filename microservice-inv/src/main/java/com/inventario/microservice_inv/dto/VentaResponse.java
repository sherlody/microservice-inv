package com.inventario.microservice_inv.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public record VentaResponse(
    @Schema(example = "10")
    Long id,

    @Schema(example = "2026-11-16T15:05:00")
    LocalDateTime fecha,

    @Schema(example = "2501.00")
    BigDecimal total,

    @Schema(description = "Detalle de los productos vendidos en este ticket")
    List<DetalleVentaResponse> detalles
) {
}