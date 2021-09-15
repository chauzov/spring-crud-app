package com.schauzov.crudapp.rest;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class ProductRestStructure {
    private Long productId;
    private Set<ProductInfoRestStructure> productInfo;
    private Set<ProductPriceRestStructure> productPrice;
    private LocalDate created;
    private LocalDate modified;

    public ProductRestStructure() {
    }

    public ProductRestStructure(
            Long productId,
            Set<ProductInfoRestStructure> productInfo,
            Set<ProductPriceRestStructure> productPrices,
            LocalDate created,
            LocalDate modified) {
        this.productId = productId;
        this.productInfo = productInfo;
        this.productPrice = productPrices;
        this.created = created;
        this.modified = modified;
    }
}
