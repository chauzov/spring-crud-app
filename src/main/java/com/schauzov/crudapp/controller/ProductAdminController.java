package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.model.Product;
import com.schauzov.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/product-admin")
public class ProductAdminController {

    private final ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "{productId}")
    public Product getProductById(@PathVariable("productId") Long id) {
        return productService.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException("The product with id " + id + " is not found")
        );
    }

    @PostMapping
    public void addProduct(@RequestBody Product product) {

        productService.addProduct(product);
    }
}
