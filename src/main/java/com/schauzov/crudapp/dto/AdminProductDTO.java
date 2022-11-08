package com.schauzov.crudapp.dto;

import lombok.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminProductDTO {
    private Long productId;
    @Valid
    private Set<ProductInfoDTO> productInfo;
    @Valid
    private Set<ProductPriceDTO> productPrices;
    private LocalDateTime created;
    private LocalDateTime modified;
}
