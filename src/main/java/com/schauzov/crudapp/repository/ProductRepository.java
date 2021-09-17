package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
