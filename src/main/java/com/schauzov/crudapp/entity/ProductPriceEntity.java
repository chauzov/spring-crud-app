package com.schauzov.crudapp.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "product_price")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPriceEntity {
    @Id
    @SequenceGenerator(name = "product_price_seq", sequenceName = "product_price_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_price_seq")
    @Column(name = "price_id", nullable = false)
    private Long priceId;
    @Column
    private Currency currency;
    @Column
    private BigDecimal price;
}
