package com.schauzov.crudapp.service;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.CustomerProductDTO;

import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface ProductService {

    AdminProductDTO getProductById(Long productId);

    long addProduct(AdminProductDTO productModel);

    void deleteProduct(Long productId);

    void replaceProduct(Long productId, AdminProductDTO productDTO);

    void editProduct(Long productId, List<JsonPatchOperation> patchOperations);

    CustomerProductDTO getProductById(Long productId, Locale locale, Currency currency);

    Set<CustomerProductDTO> getAvailableProducts(Locale locale, Currency currency, String searchString);
}
