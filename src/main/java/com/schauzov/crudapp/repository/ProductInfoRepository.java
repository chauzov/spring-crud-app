package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.entity.ProductInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfoEntity, Long> {
}
