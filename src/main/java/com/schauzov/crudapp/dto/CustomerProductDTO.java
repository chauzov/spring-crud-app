package com.schauzov.crudapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerProductDTO {
    private Long productId;
    private String name;
    private String description;
    private Locale locale;
    private BigDecimal price;
    private Currency currency;
}
