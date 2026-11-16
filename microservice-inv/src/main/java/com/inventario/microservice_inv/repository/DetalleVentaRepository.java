package com.inventario.microservice_inv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventario.microservice_inv.model.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

}