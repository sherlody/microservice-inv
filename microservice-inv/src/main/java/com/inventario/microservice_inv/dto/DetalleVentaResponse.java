package com.inventario.microservice_inv.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record DetalleVentaResponse(
    @Schema(example = "1")
    Long productoId,

    @Schema(example = "Teclado Mecánico RGB")
    String productoNombre,

    @Schema(example = "2")
    Integer cantidad,

    @Schema(example = "1250.50")
    BigDecimal precioUnitario,

    @Schema(example = "2501.00")
    BigDecimal subtotal
) {
}