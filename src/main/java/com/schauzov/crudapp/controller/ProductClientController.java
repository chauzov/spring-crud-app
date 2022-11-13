package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.dto.CustomerProductDTO;
import com.schauzov.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

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
            @RequestParam(name = "currency") String currencyCode)
    {
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        return productService.getProductById(id, locale, currency);
    }

    @GetMapping(path = "all")
    public Page<CustomerProductDTO> getProducts(
            @RequestParam Locale locale,
            @RequestParam(name = "currency")
            String currencyCode,
            @RequestParam(name = "search", defaultValue = "", required = false)
            String searchString,
            @PageableDefault(sort = {"productId"}, direction = Sort.Direction.ASC)
            Pageable pageable
    )
    {
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        return productService.getAvailableProducts(locale, currency, searchString, pageable);
    }
}
