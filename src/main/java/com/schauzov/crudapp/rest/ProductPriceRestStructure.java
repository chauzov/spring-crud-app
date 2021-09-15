package com.schauzov.crudapp.rest;

import lombok.Data;

@Data
public class ProductPriceRestStructure {
    private Integer price;
    private String currency;

    public ProductPriceRestStructure() {
    }

    public ProductPriceRestStructure(Integer price, String currency) {
        this.price = price;
        this.currency = currency;
    }
}
