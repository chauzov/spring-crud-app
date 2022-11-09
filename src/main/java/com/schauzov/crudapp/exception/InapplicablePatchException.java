package com.schauzov.crudapp.exception;

public class InapplicablePatchException extends RuntimeException {
    public InapplicablePatchException(Long id, String message) {
        super(String.format("Cannot update product with ID %d: %s", id, message));
    }
}
