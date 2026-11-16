package com.inventario.microservice_inv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventario.microservice_inv.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}