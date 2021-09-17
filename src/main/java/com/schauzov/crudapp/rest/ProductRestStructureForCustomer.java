package com.schauzov.crudapp.rest;

import lombok.Data;

@Data
public class ProductRestStructureForCustomer {
    private Long productId;
    private String name;
    private String description;
    private String locale;
    private Integer price;
    private String currency;

    public ProductRestStructureForCustomer() {
    }

    public ProductRestStructureForCustomer(
            Long productId,
            String name,
            String description,
            String locale,
            Integer price,
            String currency) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.locale = locale;
        this.price = price;
        this.currency = currency;
    }
}
