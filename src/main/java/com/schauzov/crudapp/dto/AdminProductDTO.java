package com.schauzov.crudapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;
    private LocalDateTime modified;
}
