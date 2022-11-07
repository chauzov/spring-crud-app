package com.schauzov.crudapp.dto;

import lombok.*;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductInfoDTO {
    private Long productInfoId;
    private Locale locale;
    private String name;
    private String description;
}
