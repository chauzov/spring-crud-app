package com.schauzov.crudapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import com.schauzov.crudapp.dto.ProductInfoDTO;
import com.schauzov.crudapp.dto.ProductPriceDTO;
import com.schauzov.crudapp.entity.ProductEntity;
import com.schauzov.crudapp.entity.ProductInfoEntity;
import com.schauzov.crudapp.entity.ProductPriceEntity;
import com.schauzov.crudapp.exception.IllegalProductBodyException;
import com.schauzov.crudapp.exception.InapplicablePatchException;
import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.mapper.AdminProductMapper;
import com.schauzov.crudapp.repository.ProductRepository;
import com.schauzov.crudapp.util.PatchOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final AdminProductMapper adminProductMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ObjectMapper objectMapper,
                              Validator validator,
                              AdminProductMapper adminProductMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.adminProductMapper = adminProductMapper;
    }


    public AdminProductDTO getProductById(Long id) {
        ProductEntity product = getProductEntityById(id);
        return adminProductMapper.toDto(product);
    }

    private Validator getValidator() {
        if (this.validator != null) {
            return this.validator;
        }
        // To support unit tests without app context
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public long addProduct(AdminProductDTO productDTO) {
        log.info("Adding product: " + productDTO);
        validateProductDTO(productDTO);

        ProductEntity productEntity = objectMapper.convertValue(productDTO, ProductEntity.class);
        productEntity.setCreated(LocalDateTime.now());
        productEntity.setModified(LocalDateTime.now());

        long productId = productRepository.save(productEntity).getProductId();
        log.info("The product has been saved with ID {}", productId);
        return productId;
    }

    @Override
    public void addMultipleProducts(List<AdminProductDTO> productDTOs) {
        log.info("Adding products: " + productDTOs);
        productDTOs.forEach(this::validateProductDTO);
        List<ProductEntity> productEntities = objectMapper.convertValue(productDTOs, new TypeReference<>(){});
        productEntities.forEach(p -> {
            p.setCreated(LocalDateTime.now());
            p.setModified(LocalDateTime.now());
        });
        productRepository.saveAll(productEntities);
        log.info("The products have been saved");
    }

    private void validateProductDTO(AdminProductDTO productDTO) {
        Validator productValidator = getValidator();
        Set<ConstraintViolation<AdminProductDTO>> violations = productValidator.validate(productDTO);

        if (!violations.isEmpty()) {
            StringJoiner stringJoiner = new StringJoiner(",");
            violations.forEach(v -> stringJoiner.add(v.getMessage()));
            throw new IllegalProductBodyException(stringJoiner.toString());
        }
    }

    private ProductEntity getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
        log.info("The product with ID {} has been deleted", id);
    }

    @Override
    @Transactional
    public void replaceProduct(Long productId, AdminProductDTO productDTO) {
        ProductEntity productEntity = getProductEntityById(productId);
        validateProductDTO(productDTO);
        updateProductEntityFromDTO(productEntity, productDTO);
        productRepository.save(productEntity);
        log.info("The product with ID {} has been replaced", productId);
    }

    private void updateProductEntityFromDTO(ProductEntity productEntity, AdminProductDTO productDTO) {
        replaceProductInfo(productEntity.getProductInfo(), productDTO.getProductInfo());
        replaceProductPrices(productEntity.getProductPrices(), productDTO.getProductPrices());
        productEntity.setModified(LocalDateTime.now());
    }

    private void replaceProductInfo(Set<ProductInfoEntity> existingProductInfoEntities,
                                                            Set<ProductInfoDTO> newProductInfoDTOs)
    {
        Map<Long, ProductInfoEntity> existingProductInfoMap = new HashMap<>();
        existingProductInfoEntities.forEach(pi -> existingProductInfoMap.put(pi.getProductInfoId(), pi));

        for (ProductInfoDTO productInfoDTO : newProductInfoDTOs) {
            ProductInfoEntity existingProductInfo = existingProductInfoMap.get(productInfoDTO.getProductInfoId());

            if (existingProductInfo != null) {
                // Update existing entities
                existingProductInfo.setName(productInfoDTO.getName());
                existingProductInfo.setDescription(productInfoDTO.getDescription());
                existingProductInfo.setLocale(productInfoDTO.getLocale());
                // Remove processed entries from the map
                existingProductInfoMap.remove(existingProductInfo.getProductInfoId());
            } else {
                // Does not exist - create new
                ProductInfoEntity newProductInfo = ProductInfoEntity.builder()
                        .name(productInfoDTO.getName())
                        .description(productInfoDTO.getDescription())
                        .locale(productInfoDTO.getLocale())
                        .build();
                existingProductInfoEntities.add(newProductInfo);
            }
        }
        // Unprocessed entries that were not found in the incoming DTO must be deleted
        existingProductInfoMap.forEach((productInfoId, productInfo) -> {
            log.info("Removing product info: {}", productInfo);
            existingProductInfoEntities.remove(productInfo);
        });
    }

    private void replaceProductPrices(Set<ProductPriceEntity> existingPriceEntities,
                                                               Set<ProductPriceDTO> newProductPriceDTOs)
    {
        Map<Long, ProductPriceEntity> existingProductPricesMap = new HashMap<>();
        existingPriceEntities.forEach(pp -> existingProductPricesMap.put(pp.getPriceId(), pp));

        for (ProductPriceDTO productPriceDTO : newProductPriceDTOs) {
            ProductPriceEntity existingPrice = existingProductPricesMap.get(productPriceDTO.getPriceId());

            if (existingPrice != null) {
                // Update existing entities
                existingPrice.setPrice(productPriceDTO.getPrice());
                existingPrice.setCurrency(productPriceDTO.getCurrency());
                // Remove processed entries from the map
                existingProductPricesMap.remove(productPriceDTO.getPriceId());
            } else {
                // Does not exist - create new
                ProductPriceEntity productPrice = ProductPriceEntity.builder()
                        .price(productPriceDTO.getPrice())
                        .currency(productPriceDTO.getCurrency())
                        .build();
                existingPriceEntities.add(productPrice);
            }
        }
        // Unprocessed entries that were not found in the incoming DTO must be deleted
        existingProductPricesMap.forEach((productInfoId, productPrice) -> {
            log.info("Removing product price: {}", productPrice);
            existingPriceEntities.remove(productPrice);
        });
    }

    @Override
    public void editProduct(Long productId, List<JsonPatchOperation> patchOperations) {
        validatePatchBody(productId, patchOperations);

        ProductEntity productEntity = getProductEntityById(productId);
        AdminProductDTO productDTO = objectMapper.convertValue(productEntity, AdminProductDTO.class);

        AdminProductDTO patchedProductDTO;
        JsonPatch patch = new JsonPatch(patchOperations);

        try {
            JsonNode patchedJson = patch.apply(objectMapper.convertValue(productDTO, JsonNode.class));
            patchedProductDTO = objectMapper.treeToValue(patchedJson, AdminProductDTO.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new InapplicablePatchException(productId, e.getMessage());
        }

        validateProductDTO(patchedProductDTO);
        updateProductEntityFromDTO(productEntity, patchedProductDTO);
        productRepository.save(productEntity);
        log.info("The product with ID {} has been updated", productId);
    }

    private void validatePatchBody(Long productId, List<JsonPatchOperation> patchOperations) {
        List<String> protectedPaths = Arrays.asList(
                "productId", "created", "modified", "productInfoId", "priceId");

        PatchOperation[] operations = objectMapper.convertValue(patchOperations, PatchOperation[].class);
        String errorMessage = "The following properties cannot be modified: " + Arrays.toString(protectedPaths.toArray());

        for (PatchOperation op : operations) {
            // We should not delete or replace protected properties
            if ((op.getOp().equals("remove") || op.getOp().equals("replace")) &&
                    protectedPaths.stream().anyMatch(op.getPath()::contains)) {
                throw new InapplicablePatchException(productId, errorMessage);
            }

            // We should not move protected properties
            if (op.getOp().equals("move") && protectedPaths.stream().anyMatch(op.getFrom()::contains)) {
                throw new InapplicablePatchException(productId, errorMessage);
            }

            // We should not overwrite protected properties
            if (op.getOp().equals("copy") && protectedPaths.stream().anyMatch(op.getPath()::contains)) {
                throw new InapplicablePatchException(productId, errorMessage);
            }
        }
    }

    @Override
    public CustomerProductDTO getProductById(Long productId, Locale locale, Currency currency) {
        List<CustomerProductDTO> products = productRepository
                .findProductByIdAndLocaleAndCurrency(productId, locale, currency);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(productId, locale, currency);
        }
        return products.get(0);
    }

    @Override
    public Page<CustomerProductDTO> getAvailableProducts(Locale locale, Currency currency,
                                                         String searchString, Pageable pageable) {
        Page<CustomerProductDTO> products;

        if (searchString.isEmpty()) {
            products = productRepository.findProductsByLocaleAndCurrency(locale, currency, pageable);
        } else {
            products = productRepository
                    .findProductsByLocaleAndCurrencyAndNameOrDescription(locale, currency, searchString, pageable);
        }

        if (products.isEmpty()) {
            throw new ProductNotFoundException(locale, currency, searchString);
        }
        return products;
    }
}
