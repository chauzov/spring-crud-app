package com.schauzov.crudapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schauzov.crudapp.entity.ProductEntity;
import com.schauzov.crudapp.exception.TestInitializationException;
import com.schauzov.crudapp.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ProductAdminControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductAdminControllerIntegrationTest(MockMvc mockMvc, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @AfterEach
    public void resetDb() {
        productRepository.deleteAll();
    }

    private ProductEntity addOneProductToDB(String productFilename) {
        ProductEntity productEntity;
        try {
            productEntity = objectMapper.readValue(new File(productFilename), ProductEntity.class);
        } catch (IOException ioException) {
            log.error("Could not load file {}: {}", productFilename, ioException.getMessage());
            throw new TestInitializationException();
        }
        return productRepository.save(productEntity);
    }

    private void addMultipleProductsToDb(String productFilename) {
        ProductEntity[] productEntities;
        try {
            productEntities = objectMapper.readValue(new File(productFilename), ProductEntity[].class);
        } catch (IOException ioException) {
            log.error("Could not load file {}: {}", productFilename, ioException.getMessage());
            throw new TestInitializationException();
        }
        productRepository.saveAll(Arrays.asList(productEntities));
    }

    @Test
    void getProductByIdTest() throws Exception {
        Long productId = addOneProductToDB("src/test/resources/product1.json").getProductId();

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/admin/product/{id}", productId)
                .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productId").value(productId));
    }

    @Test
    void getAllProductsByIdAndLocaleAndCurrency_Test() throws Exception {
        addMultipleProductsToDb("src/test/resources/five_products.json");

        Long productId = 2L;
        String locale = "en";
        String currency = "usd";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/customer/product/{id}?locale={locale}&currency={currency}",
                                productId, locale, currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locale").value(locale))
                .andExpect(jsonPath("$.currency").value(currency.toUpperCase()));
    }

}