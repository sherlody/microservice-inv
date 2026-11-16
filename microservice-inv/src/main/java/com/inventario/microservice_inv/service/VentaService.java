package com.inventario.microservice_inv.service;

import java.math.BigDecimal;
import java.util.List;
import com.inventario.microservice_inv.dto.DetalleVentaRequest;
import com.inventario.microservice_inv.dto.DetalleVentaResponse;
import com.inventario.microservice_inv.dto.VentaRequest;
import com.inventario.microservice_inv.dto.VentaResponse;
import com.inventario.microservice_inv.exception.ResourceNotAvailable;
import com.inventario.microservice_inv.model.DetalleVenta;
import com.inventario.microservice_inv.model.Producto;
import com.inventario.microservice_inv.model.Venta;
import com.inventario.microservice_inv.repository.ProductoRepository;
import com.inventario.microservice_inv.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> findAll() {
        return ventaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailable(
                        "No se encontró la venta con ID: " + id
                ));
        return toResponseDTO(venta);
    }

    @Transactional
    public VentaResponse registrarVenta(VentaRequest requestDTO) {
        Venta nuevaVenta = new Venta();
        BigDecimal totalVenta = BigDecimal.ZERO;

        for (DetalleVentaRequest item : requestDTO.detalles()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new ResourceNotAvailable(
                            "No se encontró el producto con ID: " + item.productoId()
                    ));

            // Validar disponibilidad en inventario
            if (producto.getStock() < item.cantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + producto.getNombre() +
                        " (Disponible: " + producto.getStock() + ")"
                );
            }

            // Descontar stock y actualizar producto
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            // Calcular subtotal del renglón
            BigDecimal subtotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.cantidad()));
            totalVenta = totalVenta.add(subtotal);

            // Armar el renglón del ticket
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);

            nuevaVenta.agregarDetalle(detalle);
        }

        nuevaVenta.setTotal(totalVenta);
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        return toResponseDTO(ventaGuardada);
    }

    // --- Métodos auxiliares privados ---

    private VentaResponse toResponseDTO(Venta venta) {
        List<DetalleVentaResponse> detallesDTO = venta.getDetalles()
                .stream()
                .map(this::toDetalleResponseDTO)
                .toList();

        return new VentaResponse(
                venta.getId(),
                venta.getFecha(),
                venta.getTotal(),
                detallesDTO
        );
    }

    private DetalleVentaResponse toDetalleResponseDTO(DetalleVenta detalle) {
        return new DetalleVentaResponse(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}

