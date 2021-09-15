package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "product_price")
public class ProductPrice {
    @Id
    @SequenceGenerator(name = "product_price_seq", sequenceName = "product_price_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_price_seq")
    @Column(name = "price_id", nullable = false)
    private Long priceId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private ProductCurrency currency;

    private Integer price;

    public ProductPrice() {
    }

    public ProductPrice(ProductCurrency currency, Integer price) {
        this.currency = currency;
        this.price = price;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public ProductCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(ProductCurrency currency) {
        this.currency = currency;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
