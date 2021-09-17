package com.schauzov.crudapp.service;

import com.schauzov.crudapp.exception.ProductNotFoundForCustomerException;
import com.schauzov.crudapp.model.*;
import com.schauzov.crudapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addOrSaveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductByIdForCustomer(Long id, String locale, String currency)
            throws ProductNotFoundForCustomerException {

        Product product = getProductById(id).orElseThrow(
                () -> new ProductNotFoundForCustomerException(id, locale, currency)
        );

        _filterLocaleAndCurrencyMutator(product, locale, currency);

        if (product.getProductInfo() == null || product.getProductPrices().isEmpty()) {
            throw new ProductNotFoundForCustomerException(id, locale, currency);
        }

        return product;
    }

    public Set<Product> getAllProductsForCustomer(String locale, String currency) {
        List<Product> allProducts = productRepository.findAll();
        Set<Product> filteredProducts = new HashSet<>();

        allProducts.forEach(p -> {
            _filterLocaleAndCurrencyMutator(p, locale, currency);
            if (!p.getProductPrices().isEmpty() && !p.getProductInfo().isEmpty()) {
                filteredProducts.add(p);
            }
        });

        return filteredProducts;
    }

    public Set<Product> getProductsBySearchStringForCustomer(String locale, String currency, String nameOrDescription) {

        Set<Product> productsFilteredByLocale
    }

    /**
     * Takes a product to input and removes all product info and prices that don't have
     * required locale and currency
     * */
    private void _filterLocaleAndCurrencyMutator(Product product, String locale, String currency) {

        Optional<ProductInfo> filteredInfo = product
                .getProductInfo()
                .stream()
                .filter(productInfo -> productInfo.getLocale().equals(locale))
                .findAny();

        Optional<ProductPrice> filteredPrices = product
                .getProductPrices()
                .stream()
                .filter(productPrice -> productPrice.getCurrency().equals(currency))
                .findAny();

        Set<ProductInfo> productInfoSet = new HashSet<>();
        filteredInfo.ifPresent(productInfoSet::add);

        Set<ProductPrice> productPrices = new HashSet<>();
        filteredPrices.ifPresent(productPrices::add);

        product.setProductInfo(productInfoSet);
        product.setProductPrices(productPrices);
    }

}
