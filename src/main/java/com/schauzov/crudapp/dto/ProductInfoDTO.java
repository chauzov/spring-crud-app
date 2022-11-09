package com.schauzov.crudapp.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductInfoDTO {
    private Long productInfoId;
    @NotNull(message = "Product info locale must be specified")
    private Locale locale;
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    private String description;
}
