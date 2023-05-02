package com.schauzov.crudapp.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private Set<ProductInfoDTO> productInfo;
    @Valid
    @NotNull
    private Set<ProductPriceDTO> productPrices;
    private LocalDateTime created;
    private LocalDateTime modified;
}
