package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "product_info")
public class ProductInfo {

    @Id
    @SequenceGenerator(name = "product_info_seq", sequenceName = "product_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_info_seq")
    @Column(name = "product_info_id", nullable = false)
    private Long productInfoId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "locale_id")
    private Locale locale;

    private String name;
    private String description;
}
