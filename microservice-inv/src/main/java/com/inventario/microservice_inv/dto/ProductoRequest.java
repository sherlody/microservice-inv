package com.inventario.microservice_inv.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
    @NotBlank(message = "El nombre del producto es obligatorio y no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(example = "Teclado Mecánico RGB")
    String nombre,

    @Size(min = 2, max = 250, message = "La descripción debe tener entre 2 y 250 caracteres")
    @Schema(example = "Teclado mecánico con retroiluminación RGB y switches de alta calidad.")
    String descripcion,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    @Schema(example = "1250.50")
    BigDecimal precio,

    @NotNull(message = "La cantidad en stock es obligatorio")
    @PositiveOrZero(message = "La cantidad en stock no puede ser negativa")
    @Schema(example = "15")
    Integer stock
) {
}
