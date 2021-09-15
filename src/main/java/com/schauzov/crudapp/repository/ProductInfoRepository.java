package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {

}
