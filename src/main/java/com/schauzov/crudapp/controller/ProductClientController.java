package com.schauzov.crudapp.controller;

import com.schauzov.crudapp.error.CustomerError;
import com.schauzov.crudapp.exception.ProductNotFoundForCustomerException;
import com.schauzov.crudapp.rest.ProductRestStructureForCustomer;
import com.schauzov.crudapp.service.ProductRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "api/v1/customer/product")
public class ProductClientController {
    private final ProductRestService productRestService;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ProductClientController(ProductRestService productRestService) {
        this.productRestService = productRestService;
    }

    @GetMapping(path = "{productId}")
    public ProductRestStructureForCustomer getProductById(
            @PathVariable("productId") Long id,
            @RequestParam(required = true) String locale,
            @RequestParam(required = true) String currency)
    {

        return productRestService.getProductForCustomer(id, locale, currency);
    }

    @GetMapping(path = "all")
    public Set<ProductRestStructureForCustomer> getProducts(
            @RequestParam(required = true) String locale,
            @RequestParam(required = true) String currency,
            @RequestParam(required = false) String searchString
    )
    {
        if (searchString != null) {
            // TODO: implement search
            //return productRestService.getProductsBySearchStringForCustomer(locale, currency, searchString);
        }
        return productRestService.getAllProductsForCustomer(locale, currency);
    }

    @ExceptionHandler(ProductNotFoundForCustomerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomerError handleProductNotFoundForCustomer(ProductNotFoundForCustomerException e) {
        logger.error("Unable to find product: " + e.getMessage());
        return new CustomerError(404, e.getMessage());
    }
}
