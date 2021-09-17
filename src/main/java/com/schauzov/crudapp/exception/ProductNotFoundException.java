package com.schauzov.crudapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends NoSuchElementException {
    public ProductNotFoundException(Long id) {
        super(String.format("The product with id %s is not found", id));
    }
}
