package com.schauzov.crudapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @OneToMany(mappedBy="product")
    private Set<ProductInfo> productInfoSet;

    @OneToMany(mappedBy="product")
    private Set<ProductPrice> productPriceSet;

    private LocalDate created;
    private LocalDate modified;

    public Long getId() {
        return productId;
    }

    public void setId(Long id) {
        this.productId = id;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }
}
