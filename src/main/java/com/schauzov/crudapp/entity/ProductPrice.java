package com.schauzov.crudapp.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Positive;

@Entity
@Table(name = "product_price")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPrice {
    @Id
    @SequenceGenerator(name = "product_price_seq", sequenceName = "product_price_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_price_seq")
    @Column(name = "price_id", nullable = false)
    private Long priceId;
    @Column
    //@NotNull(message = "Currency has to be specified")
    private Currency currency;
    @Column
    //@Positive(message = "Price has to be more than zero")
    private BigDecimal price;
}
