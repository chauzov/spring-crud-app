package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.rest.ProductRestStructure;
import com.schauzov.crudapp.service.ProductRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/admin/product")
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
    public ProductRestStructure addProduct(@RequestBody ProductRestStructure body) {
        return productRestService.addProduct(body);
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productRestService.deleteProduct(id);
    }

    @PutMapping(path = "{productId}")
    public ProductRestStructure updateProduct(@PathVariable("productId") Long id, @RequestBody ProductRestStructure body) {
        return productRestService.updateProduct(id, body);
    }
}
