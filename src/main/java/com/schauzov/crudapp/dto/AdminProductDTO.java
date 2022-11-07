package com.schauzov.crudapp.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AdminProductDTO {
    private Long productId;
    private Set<ProductInfoDTO> productInfo;
    private Set<ProductPriceDTO> productPrices;
    private LocalDateTime created;
    private LocalDateTime modified;
}
