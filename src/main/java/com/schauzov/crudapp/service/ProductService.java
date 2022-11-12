package com.schauzov.crudapp.service;

import com.github.fge.jsonpatch.JsonPatchOperation;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

public interface ProductService {

    AdminProductDTO getProductById(Long productId);

    long addProduct(AdminProductDTO productDTO);

    void addMultipleProducts(List<AdminProductDTO> productDTOs);

    void deleteProduct(Long productId);

    void replaceProduct(Long productId, AdminProductDTO productDTO);

    void editProduct(Long productId, List<JsonPatchOperation> patchOperations);

    CustomerProductDTO getProductById(Long productId, Locale locale, Currency currency);

    Page<CustomerProductDTO> getAvailableProducts(Locale locale, Currency currency, String searchString, Pageable pageable);
}
