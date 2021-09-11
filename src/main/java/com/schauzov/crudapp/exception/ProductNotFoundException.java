package com.schauzov.crudapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends NoSuchElementException {
    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
