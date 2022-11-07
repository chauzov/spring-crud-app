package com.schauzov.crudapp.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/admin/product")
public class ProductAdminController {

    private final ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "{productId}")
    public AdminProductDTO getProductById(@PathVariable("productId") Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody AdminProductDTO body) {
        productService.addProduct(body);
    }

    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping(path = "{productId}", consumes = "application/json")
    public void updateProduct(@PathVariable("productId") Long id, @RequestBody AdminProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
    }

    @PatchMapping(path = "{productId}/prices", consumes = "application/json")
    public void updateProductPrices(@PathVariable("productId") Long id, @RequestBody JsonPatch patch) {}

    @PatchMapping(path = "{productId}/info", consumes = "application/json")
    public void updateProductInfo(@PathVariable("productId") Long id, @RequestBody JsonPatch patch) {}
}
