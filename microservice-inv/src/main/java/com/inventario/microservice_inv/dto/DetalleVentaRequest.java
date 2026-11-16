package com.inventario.microservice_inv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequest(
    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser mayor a 0")
    @Schema(example = "1")
    Long productoId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser al menos 1")
    @Schema(example = "2")
    Integer cantidad
) {
}