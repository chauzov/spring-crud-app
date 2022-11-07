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
import com.schauzov.crudapp.entity.Product;
import com.schauzov.crudapp.entity.ProductInfo;
import com.schauzov.crudapp.entity.ProductPrice;
import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }


    public AdminProductDTO getProductById(Long id) {
        // Return 404 with empty body when not found
        Product productEntity = productRepository.findById(id)
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

    private Set<ProductInfoDTO> getProductInfoFromProductEntity(Product productEntity) {
        Set<ProductInfoDTO> productInfoDTOSet = new HashSet<>();
        for (ProductInfo productInfo : productEntity.getProductInfo()) {
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

    private Set<ProductPriceDTO> getProductPriceFromProductEntity(Product productEntity) {
        Set<ProductPriceDTO> productPriceDTOSet = new HashSet<>();
        for(ProductPrice productPrice : productEntity.getProductPrices()) {
            ProductPriceDTO productPriceDTO = ProductPriceDTO.builder()
                    .priceId(productPrice.getPriceId())
                    .price(productPrice.getPrice())
                    .currency(productPrice.getCurrency())
                    .build();
            productPriceDTOSet.add(productPriceDTO);
        }
        return productPriceDTOSet;
    }

    @Override
    public void addProduct(AdminProductDTO productDTO) {
        log.info("Adding product: " + productDTO);
        Set<ProductInfo> productInfoSet = getProductInfoFromProductDTO(productDTO);
        Set<ProductPrice> productPriceSet = getProductPricesFromProductDTO(productDTO);

        Product productEntity = Product.builder()
                .productInfo(productInfoSet)
                .productPrices(productPriceSet)
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();

        productRepository.save(productEntity);
    }

    private Set<ProductInfo> getProductInfoFromProductDTO(AdminProductDTO productDTO) {
        Set<ProductInfo> productInfoSet = new HashSet<>();
        for (ProductInfoDTO productInfoDTO : productDTO.getProductInfo()) {
            ProductInfo productInfo = ProductInfo.builder()
                    .productInfoId(productInfoDTO.getProductInfoId())
                    .name(productInfoDTO.getName())
                    .description(productInfoDTO.getDescription())
                    .locale(productInfoDTO.getLocale())
                    .build();
            productInfoSet.add(productInfo);
        }
        return productInfoSet;
    }

    private Set<ProductPrice> getProductPricesFromProductDTO(AdminProductDTO productDTO) {
        Set<ProductPrice> productPriceSet = new HashSet<>();
        for (ProductPriceDTO productPriceDTO : productDTO.getProductPrices()) {
            ProductPrice productPrice = ProductPrice.builder()
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
    public void updateProduct(Long productId, AdminProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        updateProductInfo(product.getProductInfo(), productDTO.getProductInfo());
        updateProductPrices(product.getProductPrices(), productDTO.getProductPrices());
        product.setModified(LocalDateTime.now());
        productRepository.save(product);
    }

    private void updateProductInfo(Set<ProductInfo> existingProductInfoSet, Set<ProductInfoDTO> productInfoDTOSet) {
        Map<Long, ProductInfo> existingProductInfoMap = new HashMap<>();
        existingProductInfoSet.forEach(pi -> existingProductInfoMap.put(pi.getProductInfoId(), pi));

        for (ProductInfoDTO productInfoDTO : productInfoDTOSet) {
            ProductInfo existingProductInfo = existingProductInfoMap.get(productInfoDTO.getProductInfoId());

            if (existingProductInfo != null) {
                existingProductInfo.setName(productInfoDTO.getName());
                existingProductInfo.setDescription(productInfoDTO.getDescription());
                existingProductInfo.setLocale(productInfoDTO.getLocale());
            } else {
                ProductInfo newProductInfo = ProductInfo.builder()
                        .name(productInfoDTO.getName())
                        .description(productInfoDTO.getDescription())
                        .locale(productInfoDTO.getLocale())
                        .build();
                existingProductInfoSet.add(newProductInfo);
            }
        }
    }

    private void updateProductPrices(Set<ProductPrice> existingPrices, Set<ProductPriceDTO> productPriceDTOSet) {
        Map<Long, ProductPrice> existingProductPricesMap = new HashMap<>();
        existingPrices.forEach(pp -> existingProductPricesMap.put(pp.getPriceId(), pp));

        for (ProductPriceDTO productPriceDTO : productPriceDTOSet) {
            ProductPrice existingPrice = existingProductPricesMap.get(productPriceDTO.getPriceId());

            if (existingPrice != null) {
                existingPrice.setPrice(productPriceDTO.getPrice());
                existingPrice.setCurrency(productPriceDTO.getCurrency());
            } else {
                ProductPrice productPrice = ProductPrice.builder()
                        .price(productPriceDTO.getPrice())
                        .currency(productPriceDTO.getCurrency())
                        .build();
                existingPrices.add(productPrice);
            }
        }
    }

    @Override
    public void updateProductPrices(Long productId, JsonPatch patch) {

    }

    @Override
    public void updateProductInfo(Long productId, JsonPatch patch) {

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
        return null;
    }
}