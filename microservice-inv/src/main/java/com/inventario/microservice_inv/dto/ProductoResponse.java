package com.inventario.microservice_inv.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProductoResponse(
    @Schema(example = "1")
        Long id,

        @Schema(example = "Teclado Mecánico RGB")
        String nombre,

        @Schema(example = "Teclado mecánico con retroiluminación RGB y switches de alta calidad.")
        String descripcion,

        @Schema(example = "1250.50")
        BigDecimal precio,

        @Schema(example = "15")
        Integer stock
) {
}
