package com.schauzov.crudapp.rest;

import lombok.Data;

@Data
public class ProductInfoRestStructure {
    private String locale;
    private String name;
    private String description;

    public ProductInfoRestStructure() {
    }

    public ProductInfoRestStructure(String locale, String name, String description) {
        this.locale = locale;
        this.name = name;
        this.description = description;
    }
}
