package com.schauzov.crudapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Locale;
//import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoEntity {
    @Id
    @SequenceGenerator(name = "product_info_seq", sequenceName = "product_info_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_info_seq")
    @Column(name = "product_info_id")
    private Long productInfoId;
    @Column
    //@NotNull(message = "Name cannot be null")
    private String name;
    @Column
    private String description;
    @Column
    //@NotNull(message = "locale cannot be null")
    private Locale locale;
}
