package com.schauzov.crudapp.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.http.AppResponse;
import com.schauzov.crudapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<AppResponse> addProduct(@RequestBody AdminProductDTO body) {
        long productId = productService.addProduct(body);
        AppResponse appResponse = new AppResponse(HttpStatus.CREATED.value(), "Added product with ID " + productId);
        return new ResponseEntity<>(appResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "{productId}")
    public ResponseEntity<AppResponse> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(), "Deleted product with ID " + id);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PutMapping(path = "{productId}", consumes = "application/json")
    public ResponseEntity<AppResponse> replaceProduct(@PathVariable("productId") Long id,
                                                      @RequestBody AdminProductDTO productDTO) {
        productService.replaceProduct(id, productDTO);
        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(), "Replaced product with ID " + id);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }

    @PatchMapping(path = "{productId}", consumes = "application/json-patch+json")
    public ResponseEntity<AppResponse> editProduct(@PathVariable("productId") Long id,
                                                   @RequestBody List<JsonPatchOperation> patchOperations) {
        productService.editProduct(id, patchOperations);
        AppResponse appResponse = new AppResponse(HttpStatus.OK.value(), "Updated product with ID " + id);
        return new ResponseEntity<>(appResponse, HttpStatus.OK);
    }
}
