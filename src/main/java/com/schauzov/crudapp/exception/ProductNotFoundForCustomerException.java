package com.schauzov.crudapp.exception;

import java.util.NoSuchElementException;

public class ProductNotFoundForCustomerException extends NoSuchElementException {
    public ProductNotFoundForCustomerException(Long id, String locale, String currency) {
        super(String.format(
                "The product with id %d is not found for locale %s and currency %s", id, locale, currency));
    }
}
