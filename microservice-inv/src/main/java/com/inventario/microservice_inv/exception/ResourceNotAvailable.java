package com.inventario.microservice_inv.exception;

public class ResourceNotAvailable extends RuntimeException {
    public ResourceNotAvailable(String message) {
        super(message);
    }
}
