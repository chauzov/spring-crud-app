package com.schauzov.crudapp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductPriceDTO {
    private Long priceId;
    private BigDecimal price;
    private Currency currency;
}
