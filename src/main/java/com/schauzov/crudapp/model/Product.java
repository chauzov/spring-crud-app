package com.schauzov.crudapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @Column(name = "product_id")
    private Long productId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<ProductInfo> productInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Set<ProductPrice> productPrices;

    private LocalDate created;
    private LocalDate modified;

    public Product() {
    }

    public Product(Set<ProductInfo> productInfo, Set<ProductPrice> productPrices, LocalDate created, LocalDate modified) {
        this.productInfo = productInfo;
        this.productPrices = productPrices;
        this.created = created;
        this.modified = modified;
    }

    public Product(Long productId, Set<ProductInfo> productInfo, Set<ProductPrice> productPrices, LocalDate created, LocalDate modified) {
        this.productId = productId;
        this.productInfo = productInfo;
        this.productPrices = productPrices;
        this.created = created;
        this.modified = modified;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Set<ProductInfo> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(Set<ProductInfo> productInfo) {
        this.productInfo = productInfo;
    }

    public Set<ProductPrice> getProductPrices() {
        return productPrices;
    }

    public void setProductPrices(Set<ProductPrice> productPrices) {
        this.productPrices = productPrices;
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
