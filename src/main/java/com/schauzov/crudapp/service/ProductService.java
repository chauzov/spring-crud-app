package com.schauzov.crudapp.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import com.schauzov.crudapp.dto.ProductPriceDTO;

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

public interface ProductService {

    AdminProductDTO getProductById(Long productId);

    void addProduct(AdminProductDTO productModel);

    void deleteProduct(Long productId);

    void updateProduct(Long productId, AdminProductDTO productDTO);

    void updateProductPrices(Long productId, JsonPatch patch);

    void updateProductInfo(Long productId, JsonPatch patch);

    CustomerProductDTO getProductById(Long productId, Locale locale, Currency currency);

    Set<CustomerProductDTO> getAvailableProducts(Locale locale, Currency currency, String searchString);
}
