package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.ProductCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<ProductCurrency, Long> {
}
