package com.schauzov.crudapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.CustomerProductDTO;
import com.schauzov.crudapp.dto.ProductInfoDTO;
import com.schauzov.crudapp.dto.ProductPriceDTO;
import com.schauzov.crudapp.entity.ProductEntity;
import com.schauzov.crudapp.entity.ProductInfoEntity;
import com.schauzov.crudapp.entity.ProductPriceEntity;
import com.schauzov.crudapp.exception.IllegalProductBodyException;
import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    private final Validator validator;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ObjectMapper objectMapper, Validator validator) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }


    public AdminProductDTO getProductById(Long id) {
        // Return 404 with empty body when not found
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Set<ProductInfoDTO> productInfoDTOSet = getProductInfoFromProductEntity(productEntity);
        Set<ProductPriceDTO> productPriceDTOSet = getProductPriceFromProductEntity(productEntity);

        return AdminProductDTO.builder()
                .productId(productEntity.getProductId())
                .created(productEntity.getCreated())
                .modified(productEntity.getModified())
                .productInfo(productInfoDTOSet)
                .productPrices(productPriceDTOSet)
                .build();
    }

    private Set<ProductInfoDTO> getProductInfoFromProductEntity(ProductEntity productEntity) {
        Set<ProductInfoDTO> productInfoDTOSet = new HashSet<>();
        for (ProductInfoEntity productInfo : productEntity.getProductInfo()) {
            ProductInfoDTO productInfoDTO = ProductInfoDTO.builder()
                    .productInfoId(productInfo.getProductInfoId())
                    .name(productInfo.getName())
                    .locale(productInfo.getLocale())
                    .description(productInfo.getDescription())
                    .build();
            productInfoDTOSet.add(productInfoDTO);
        }
        return productInfoDTOSet;
    }

    private Set<ProductPriceDTO> getProductPriceFromProductEntity(ProductEntity productEntity) {
        Set<ProductPriceDTO> productPriceDTOSet = new HashSet<>();
        for(ProductPriceEntity productPrice : productEntity.getProductPrices()) {
            ProductPriceDTO productPriceDTO = ProductPriceDTO.builder()
                    .priceId(productPrice.getPriceId())
                    .price(productPrice.getPrice())
                    .currency(productPrice.getCurrency())
                    .build();
            productPriceDTOSet.add(productPriceDTO);
        }
        return productPriceDTOSet;
    }

    private Validator getValidator() {
        if (this.validator != null) {
            return this.validator;
        }
        // To support unit tests without app context
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public void addProduct(AdminProductDTO productDTO) {
        log.info("Adding product: " + productDTO);
        validateProductDTO(productDTO);

        Set<ProductInfoEntity> productInfoSet = getProductInfoFromProductDTO(productDTO);
        Set<ProductPriceEntity> productPriceSet = getProductPricesFromProductDTO(productDTO);

        ProductEntity productEntity = ProductEntity.builder()
                .productInfo(productInfoSet)
                .productPrices(productPriceSet)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();

        productRepository.save(productEntity);
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

    private Set<ProductInfoEntity> getProductInfoFromProductDTO(AdminProductDTO productDTO) {
        Set<ProductInfoEntity> productInfoSet = new HashSet<>();
        for (ProductInfoDTO productInfoDTO : productDTO.getProductInfo()) {
            ProductInfoEntity productInfo = ProductInfoEntity.builder()
                    .productInfoId(productInfoDTO.getProductInfoId())
                    .name(productInfoDTO.getName())
                    .description(productInfoDTO.getDescription())
                    .locale(productInfoDTO.getLocale())
                    .build();
            productInfoSet.add(productInfo);
        }
        return productInfoSet;
    }

    private Set<ProductPriceEntity> getProductPricesFromProductDTO(AdminProductDTO productDTO) {
        Set<ProductPriceEntity> productPriceSet = new HashSet<>();
        for (ProductPriceDTO productPriceDTO : productDTO.getProductPrices()) {
            ProductPriceEntity productPrice = ProductPriceEntity.builder()
                    .priceId(productPriceDTO.getPriceId())
                    .currency(productPriceDTO.getCurrency())
                    .price(productPriceDTO.getPrice())
                    .build();
            productPriceSet.add(productPrice);
        }
        return productPriceSet;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void replaceProduct(Long productId, AdminProductDTO productDTO) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        replaceProductInfo(productEntity.getProductInfo(), productDTO.getProductInfo());
        replaceProductPrices(productEntity.getProductPrices(), productDTO.getProductPrices());

        productEntity.setModified(LocalDateTime.now());

        productRepository.save(productEntity);
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
    public void editProduct(Long productId, JsonPatch patch) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // TODO: implement patching
    }

    private AdminProductDTO applyPatchToProduct(JsonPatch patch, AdminProductDTO productDTO)
            throws JsonPatchException, JsonProcessingException {
        JsonNode patchedProductDTO = patch.apply(objectMapper.convertValue(productDTO, JsonNode.class));
        return objectMapper.treeToValue(patchedProductDTO, AdminProductDTO.class);
    }

    @Override
    public CustomerProductDTO getProductById(Long productId, Locale locale, Currency currency) {
        List<CustomerProductDTO> products = productRepository
                .findProductByProductIdAndLocaleAndCurrency(productId, locale, currency);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(productId, locale, currency);
        }
        return products.get(0);
    }

    @Override
    public Set<CustomerProductDTO> getAvailableProducts(Locale locale, Currency currency, String searchString) {
        // TODO: implement search
        return new HashSet<>();
    }
}
