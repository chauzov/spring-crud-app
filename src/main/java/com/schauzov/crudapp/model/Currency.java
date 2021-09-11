package com.schauzov.crudapp.model;

import javax.persistence.*;

@Entity
@Table(name = "currency")
public class Currency {
    @Id
    @SequenceGenerator(name = "currency_seq", sequenceName = "currency_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq")
    @Column(name = "currency_id", nullable = false)
    private Long currencyId;

    @Column(name = "currency_name")
    private String currencyName;
}
