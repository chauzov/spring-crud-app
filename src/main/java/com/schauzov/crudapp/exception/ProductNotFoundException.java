package com.schauzov.crudapp.exception;

import java.util.Currency;
import java.util.Locale;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(String.format("The product with id '%s' is not found", id));
    }

    public ProductNotFoundException(Locale locale, Currency currency, String searchString) {
        super(String.format(
                "No products were found for locale '%s', currency '%s' and search string '%s'",
                locale, currency, searchString));
    }

    public ProductNotFoundException(Long id, Locale locale, Currency currency) {
        super(String.format(
                "The product with id '%d' is not found for locale '%s' and currency '%s'", id, locale, currency));
    }
}
