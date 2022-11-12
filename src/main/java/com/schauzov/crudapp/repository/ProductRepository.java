package com.schauzov.crudapp.repository;

import com.schauzov.crudapp.entity.ProductEntity;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT new com.schauzov.crudapp.dto.CustomerProductDTO(p.productId, pi.name, pi.description, pi.locale, " +
            "pp.price, pp.currency) FROM ProductEntity p JOIN p.productInfo pi JOIN p.productPrices pp " +
            "WHERE  p.productId = ?1 and pi.locale = ?2 and pp.currency = ?3")
    List<CustomerProductDTO> findProductByIdAndLocaleAndCurrency(Long productId, Locale locale, Currency currency);

    @Query("SELECT new com.schauzov.crudapp.dto.CustomerProductDTO(p.productId, pi.name, pi.description, pi.locale, " +
            "pp.price, pp.currency) FROM ProductEntity p JOIN p.productInfo pi JOIN p.productPrices pp " +
            "WHERE  pi.locale = ?1 and pp.currency = ?2")
    Page<CustomerProductDTO> findProductsByLocaleAndCurrency(Locale locale, Currency currency, Pageable pageable);

    @Query("SELECT new com.schauzov.crudapp.dto.CustomerProductDTO(p.productId, pi.name, pi.description, pi.locale, " +
            "pp.price, pp.currency) FROM ProductEntity p JOIN p.productInfo pi JOIN p.productPrices pp " +
            "WHERE  pi.locale = ?1 AND pp.currency = ?2 " +
            "AND (lower(pi.name) like lower(concat('%', ?3, '%')) OR lower(pi.description) like lower(concat('%', ?3, '%')))")
    Page<CustomerProductDTO> findProductsByLocaleAndCurrencyAndNameOrDescription(Locale locale, Currency currency,
                                                                                 String searchString, Pageable pageable);
}
