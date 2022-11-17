package com.schauzov.crudapp.service;

import com.schauzov.crudapp.dto.AdminProductDTO;
import com.schauzov.crudapp.dto.ProductInfoDTO;
import com.schauzov.crudapp.dto.ProductPriceDTO;
import com.schauzov.crudapp.exception.IllegalProductBodyException;
import com.schauzov.crudapp.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    private AdminProductDTO getDefaultAdminProductDTO() {
        ProductInfoDTO productInfoDTO = ProductInfoDTO.builder()
                .name("Product name")
                .description("Product description")
                .locale(Locale.ENGLISH)
                .build();
        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder()
                .price(BigDecimal.valueOf(15))
                .currency(Currency.getInstance("USD"))
                .build();

        return AdminProductDTO.builder()
                .productInfo(Set.of(productInfoDTO))
                .productPrices(Set.of(productPriceDTO))
                .build();
    }

    @Test
    void cannotAddProductWithoutName_Test() {
        AdminProductDTO productDTO = getDefaultAdminProductDTO();
        productDTO.getProductInfo().forEach(pi -> pi.setName(null));

        IllegalProductBodyException exception = Assertions.assertThrows(
                IllegalProductBodyException.class,
                () -> productService.addProduct(productDTO));
        assertThat(exception.getMessage(), containsString("Product name cannot be blank"));
    }

    @Test
    void cannotAddProductWithBlankName_Test() {
        AdminProductDTO productDTO = getDefaultAdminProductDTO();
        productDTO.getProductInfo().forEach(pi -> pi.setName(" "));

        IllegalProductBodyException exception = Assertions.assertThrows(
                IllegalProductBodyException.class,
                () -> productService.addProduct(productDTO));
        assertThat(exception.getMessage(), containsString("Product name cannot be blank"));
    }

    @Test
    void productPriceCannotBeZero_Test() {
        AdminProductDTO productDTO = getDefaultAdminProductDTO();
        productDTO.getProductPrices().forEach(pp -> pp.setPrice(BigDecimal.valueOf(0)));

        IllegalProductBodyException exception = Assertions.assertThrows(
                IllegalProductBodyException.class,
                () -> productService.addProduct(productDTO));
        assertThat(exception.getMessage(), containsString("Product price must be greater than zero"));
    }

    @Test
    void productPriceCannotBeNegative_Test() {
        AdminProductDTO productDTO = getDefaultAdminProductDTO();
        productDTO.getProductPrices().forEach(pp -> pp.setPrice(BigDecimal.valueOf(-1)));

        IllegalProductBodyException exception = Assertions.assertThrows(
                IllegalProductBodyException.class,
                () -> productService.addProduct(productDTO));
        assertThat(exception.getMessage(), containsString("Product price must be greater than zero"));
    }

    @Test
    void productPriceCannotBeNull_Test() {
        AdminProductDTO productDTO = getDefaultAdminProductDTO();
        productDTO.getProductPrices().forEach(pp -> pp.setPrice(null));

        IllegalProductBodyException exception = Assertions.assertThrows(
                IllegalProductBodyException.class,
                () -> productService.addProduct(productDTO));
        assertThat(exception.getMessage(), containsString("Product price cannot be null"));
    }
}