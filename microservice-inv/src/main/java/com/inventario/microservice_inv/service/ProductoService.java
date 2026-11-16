package com.inventario.microservice_inv.service;

import java.util.List;
import com.inventario.microservice_inv.dto.ProductoRequest;
import com.inventario.microservice_inv.dto.ProductoResponse;
import com.inventario.microservice_inv.exception.ResourceNotAvailable;
import com.inventario.microservice_inv.model.Producto;
import com.inventario.microservice_inv.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = buscarEntidadPorId(id);
        return toResponseDTO(producto);
    }

    @Transactional
    public ProductoResponse saveProducto(ProductoRequest requestDTO) {
        Producto nuevoProducto = toEntity(requestDTO);
        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return toResponseDTO(productoGuardado);
    }

    @Transactional
    public ProductoResponse updateProducto(Long id, ProductoRequest requestDTO) {
        Producto productoExistente = buscarEntidadPorId(id);

        productoExistente.setNombre(requestDTO.nombre());
        productoExistente.setDescripcion(requestDTO.descripcion());
        productoExistente.setPrecio(requestDTO.precio());
        productoExistente.setStock(requestDTO.stock());

        Producto productoActualizado = productoRepository.save(productoExistente);
        return toResponseDTO(productoActualizado);
    }

    @Transactional
    public void deleteProducto(Long id) {
        Producto productoExistente = buscarEntidadPorId(id);
        productoRepository.delete(productoExistente);
    }

    // --- Métodos auxiliares privados ---

    private Producto buscarEntidadPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotAvailable(
                        "No se encontró el producto con ID: " + id
                ));
    }

    private Producto toEntity(ProductoRequest dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        return producto;
    }

    private ProductoResponse toResponseDTO(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
        );
    }
}
