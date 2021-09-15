package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.rest.ProductRestStructure;
import com.schauzov.crudapp.service.ProductRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/product-admin")
public class ProductAdminController {

    private final ProductRestService productRestService;

    @Autowired
    public ProductAdminController(ProductRestService productRestService) {
        this.productRestService = productRestService;
    }

    @GetMapping(path = "{productId}")
    public ProductRestStructure getProductById(@PathVariable("productId") Long id) {
        return productRestService.getProductById(id);
    }

    @PostMapping
    public ProductRestStructure addProduct(@RequestBody ProductRestStructure product) {
        return productRestService.addProduct(product);
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productRestService.deleteProduct(id);
    }
}
