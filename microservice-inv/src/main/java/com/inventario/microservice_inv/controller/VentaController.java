package com.inventario.microservice_inv.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventario.microservice_inv.dto.ErrorResponse;
import com.inventario.microservice_inv.dto.VentaRequest;
import com.inventario.microservice_inv.dto.VentaResponse;
import com.inventario.microservice_inv.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Operaciones para el registro de ventas, generación de tickets y consulta de historial")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // ENDPOINT LISTAR TODAS LAS VENTAS
    @Operation(
            summary = "Listar todas las ventas",
            description = "Obtiene el historial completo de tickets de venta registrados en PostgreSQL. Devuelve un arreglo vacío si no hay registros."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de ventas recuperada correctamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = VentaResponse.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    // OBTENER LA INFORMACION DE UNA VENTA POR SU ID
    @Operation(
            summary = "Obtener ticket de venta por ID",
            description = "Busca y devuelve el detalle completo de un ticket de venta específico a partir de su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket de venta encontrado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VentaResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe una venta con el ID proporcionado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    // ENDPOINT PARA REGISTRAR UNA NUEVA VENTA Y GENERAR EL TICKET
    @Operation(
            summary = "Registrar una nueva venta",
            description = "Valida la disponibilidad de stock, descuenta las unidades del inventario, calcula el total y guarda el ticket en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Venta registrada y ticket generado exitosamente",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VentaResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos (lista vacía, cantidades negativas o stock insuficiente)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Alguno de los productos incluidos en la venta no existe en la base de datos",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<VentaResponse> crearVenta(
            @Valid @RequestBody VentaRequest request) {
        VentaResponse nuevaVenta = ventaService.registrarVenta(request);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }
}