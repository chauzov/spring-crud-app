package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.entity.ProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, Long> {
}
