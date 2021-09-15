package com.schauzov.crudapp.service;

import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.model.*;
import com.schauzov.crudapp.rest.ProductInfoRestStructure;
import com.schauzov.crudapp.rest.ProductPriceRestStructure;
import com.schauzov.crudapp.rest.ProductRestStructure;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProductRestService {
    private final ProductService productService;

    @Autowired
    public ProductRestService(ProductService productService) {
        this.productService = productService;
    }

    public ProductRestStructure getProductById(Long id) {
        Product product = productService.getProductById(id).orElseThrow(
                () -> new ProductNotFoundException("The product with id " + id + " is not found")
        );

        return _convertProductToRest(product);
    }

    public ProductRestStructure addProduct(ProductRestStructure productRestStructure) {

        return _convertProductToRest(
                productService.addOrSaveProduct(
                        _convertRestToProduct(null, LocalDate.now(), productRestStructure)
                ));
    }

    public void deleteProduct(Long id) {
        if ( productService.getProductById(id).isEmpty()) {
            throw new ProductNotFoundException("The product with id " + id + " is not found");
        }
        productService.deleteProduct(id);
    }

    public ProductRestStructure updateProduct(Long productId, ProductRestStructure productRestStructure) {
        Product currentProduct = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("The product with id " + productId + " is not found"));

        Product product = _convertRestToProduct(
                productId,
                currentProduct.getCreated(),
                productRestStructure);

        return _convertProductToRest(productService.addOrSaveProduct(product));
    }

    private ProductRestStructure _convertProductToRest(Product product) {
        Set<ProductInfo> productInfoSet = product.getProductInfo();
        Set<ProductPrice> productPrices = product.getProductPrices();

        Set<ProductInfoRestStructure> productInfoRestStructureSet = new HashSet<>();
        Set<ProductPriceRestStructure> productPriceRestStructureSet = new HashSet<>();

        productInfoSet.forEach(pi -> productInfoRestStructureSet.add(new ProductInfoRestStructure(
                pi.getLocale(),
                pi.getName(),
                pi.getDescription()
        )));

        productPrices.forEach(pp -> productPriceRestStructureSet.add(new ProductPriceRestStructure(
                pp.getPrice(),
                pp.getCurrency()
        )));

        return new ProductRestStructure(
                product.getProductId(),
                productInfoRestStructureSet,
                productPriceRestStructureSet,
                product.getCreated(),
                product.getModified()
        );
    }

    private Product _convertRestToProduct(
            Long productId,
            LocalDate created,
            ProductRestStructure body) {

        // Prepare set with product info
        Set<ProductInfo> productInfoSet = new HashSet<>();
        body.getProductInfo().forEach(pi -> productInfoSet.add(new ProductInfo(
                pi.getLocale(),
                pi.getName(),
                pi.getDescription()
        )));

        // Prepare set with product prices
        Set<ProductPrice> productPriceSet = new HashSet<>();
        body.getProductPrice().forEach(pp -> productPriceSet.add(new ProductPrice(
                pp.getCurrency(),
                pp.getPrice()
        )));

        return new Product(productId, productInfoSet, productPriceSet, created, LocalDate.now());

    }
}
