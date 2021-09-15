package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<ProductPrice, Long> {
}
