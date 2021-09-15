package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "product_info")
public class ProductInfo {

    @Id
    @SequenceGenerator(name = "product_info_seq", sequenceName = "product_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_info_seq")
    @Column(name = "product_info_id")
    private Long productInfoId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String locale;

    public ProductInfo() {
    }

    public ProductInfo(Long productInfoId, String name, String description, String locale) {
        this.productInfoId = productInfoId;
        this.name = name;
        this.description = description;
        this.locale = locale;
    }

    public ProductInfo(String locale, String name, String description) {
        this.locale = locale;
        this.name = name;
        this.description = description;
    }

    public Long getProductInfoId() {
        return productInfoId;
    }

    public void setProductInfoId(Long productInfoId) {
        this.productInfoId = productInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
