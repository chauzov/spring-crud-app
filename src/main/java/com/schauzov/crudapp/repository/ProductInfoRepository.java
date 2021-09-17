package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
}
