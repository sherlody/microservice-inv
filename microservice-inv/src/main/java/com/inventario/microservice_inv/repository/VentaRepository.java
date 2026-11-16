package com.inventario.microservice_inv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventario.microservice_inv.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

}