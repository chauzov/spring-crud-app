package com.schauzov.crudapp.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductPriceDTO {
    private Long priceId;
    @NotNull(message = "Product price cannot be null")
    @Positive(message = "Product price must be greater than zero")
    private BigDecimal price;
    @NotNull(message = "Product price currency must be specified")
    private Currency currency;
}
