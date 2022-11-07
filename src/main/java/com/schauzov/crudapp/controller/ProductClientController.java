package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.dto.CustomerProductDTO;
import com.schauzov.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/customer/product")
public class ProductClientController {
    private final ProductService productService;

    @Autowired
    public ProductClientController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "{productId}")
    public CustomerProductDTO getProductById(
            @PathVariable("productId") Long id,
            @RequestParam Locale locale,
            @RequestParam Currency currency)
    {
        return productService.getProductById(id, locale, currency);
    }

    @GetMapping(path = "all")
    public Set<CustomerProductDTO> getProducts(
            @RequestParam Locale locale,
            @RequestParam Currency currency,
            @RequestParam(required = false) String searchString
    )
    {
        if (searchString == null) {
            searchString = "";
        }
        return productService.getAvailableProducts(locale, currency, searchString);
    }
}
