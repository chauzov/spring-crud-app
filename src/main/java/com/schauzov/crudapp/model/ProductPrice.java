package com.schauzov.crudapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "product_price")
public class ProductPrice {
    @Id
    @SequenceGenerator(name = "product_price_seq", sequenceName = "product_price_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_price_seq")
    @Column(name = "price_id", nullable = false)
    private Long priceId;
    @Column
    @NotNull(message = "Currency has to be specified")
    private String currency;
    @Column
    @Positive(message = "Price has to be more than zero")
    private Integer price;

    public ProductPrice() {
    }

    public ProductPrice(String currency, Integer price) {
        this.currency = currency;
        this.price = price;
    }


    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
