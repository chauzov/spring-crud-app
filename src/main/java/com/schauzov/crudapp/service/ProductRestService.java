package com.schauzov.crudapp.service;

import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.exception.ProductNotFoundForCustomerException;
import com.schauzov.crudapp.model.Product;
import com.schauzov.crudapp.model.ProductInfo;
import com.schauzov.crudapp.model.ProductPrice;
import com.schauzov.crudapp.rest.ProductInfoRestStructure;
import com.schauzov.crudapp.rest.ProductPriceRestStructure;
import com.schauzov.crudapp.rest.ProductRestStructure;
import com.schauzov.crudapp.rest.ProductRestStructureForCustomer;
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
                () -> new ProductNotFoundException(id)
        );
        return _convertProductToRestForAdmin(product);
    }

    public ProductRestStructure addProduct(ProductRestStructure productRestStructure) {
        return _convertProductToRestForAdmin(
                productService.addOrSaveProduct(
                        _convertRestToProductForAdmin(null, LocalDate.now(), productRestStructure)
                ));
    }

    public void deleteProduct(Long id) {
        if (productService.getProductById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        productService.deleteProduct(id);
    }

    public ProductRestStructure updateProduct(Long productId, ProductRestStructure productRestStructure) {
        Product currentProduct = productService.getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Product product = _convertRestToProductForAdmin(
                productId,
                currentProduct.getCreated(),
                productRestStructure);

        return _convertProductToRestForAdmin(productService.addOrSaveProduct(product));
    }

    public ProductRestStructureForCustomer getProductForCustomer(Long id, String locale, String currency)
            throws ProductNotFoundForCustomerException {

        Product product = productService.getProductByIdForCustomer(id, locale, currency);
        return _convertProductToRestForCustomer(product);
    }

    public Set<ProductRestStructureForCustomer> getAllProductsForCustomer(String locale, String currency) {
        Set<ProductRestStructureForCustomer> productsForCustomer = new HashSet<>();
        productService.getAllProductsForCustomer(locale, currency)
                .forEach(p -> productsForCustomer.add(_convertProductToRestForCustomer(p)));
        return productsForCustomer;
    }


    private ProductRestStructureForCustomer _convertProductToRestForCustomer(Product product) {
        return new ProductRestStructureForCustomer(
                product.getProductId(),
                product.getProductInfo().stream().findAny().get().getName(),
                product.getProductInfo().stream().findAny().get().getDescription(),
                product.getProductInfo().stream().findAny().get().getLocale(),
                product.getProductPrices().stream().findAny().get().getPrice(),
                product.getProductPrices().stream().findAny().get().getCurrency()
        );
    }


    private ProductRestStructure _convertProductToRestForAdmin(Product product) {
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


    private Product _convertRestToProductForAdmin(
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
