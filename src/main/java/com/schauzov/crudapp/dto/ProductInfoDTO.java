package com.schauzov.crudapp.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductInfoDTO {
    private Long productInfoId;
    private Locale locale;
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    private String description;
}
