package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "currency",
        uniqueConstraints = {@UniqueConstraint(name = "currency_name_uniq", columnNames = {"currency_name"})}
)
public class ProductCurrency {
    @Id
    @SequenceGenerator(name = "currency_seq", sequenceName = "currency_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "currency_name")
    private String currencyName;


    public ProductCurrency() {
    }

    public ProductCurrency(String currencyName) {
        this.currencyName = currencyName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
