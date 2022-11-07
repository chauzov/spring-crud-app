package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.entity.ProductEntity;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query(value = "select p.product_id, pi.name, pi.description, pi.locale, pp.price, pp.currency" +
            " from product p, product_info pi, product_price pp" +
            " where p.product_id = pi.product_id and p.product_id = pp.product_id and" +
            " p.product_id = ?1 and pi.locale = ?2 and pp.currency = ?3", nativeQuery = true)
    List<CustomerProductDTO> findProductByProductIdAndLocaleAndCurrency(Long productId, Locale locale, Currency currency);
}
