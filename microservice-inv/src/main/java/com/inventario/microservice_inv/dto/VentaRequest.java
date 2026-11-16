package com.inventario.microservice_inv.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record VentaRequest(
    @NotEmpty(message = "La venta debe incluir al menos un producto")
    @Valid
    @Schema(description = "Lista de productos y cantidades incluidas en el ticket")
    List<DetalleVentaRequest> detalles
) {
}
