package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.Product;

import java.util.Set;

public interface ProductRepositoryForCustomer {
    Set<Product> findProductsWithLocaleAndCurrency(String locale, String currency);
}
