package com.schauzov.crudapp.exception.handling;

import com.schauzov.crudapp.exception.IllegalProductBodyException;
import com.schauzov.crudapp.exception.InapplicablePatchException;
import com.schauzov.crudapp.exception.ProductNotFoundException;
import com.schauzov.crudapp.http.AppResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AppResponse> handleProductNotFound(ProductNotFoundException e) {
        log.error(e.getMessage(), e);
        AppResponse appResponse = new AppResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(appResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalProductBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppResponse> handleIllegalProductBodyExceptionn(IllegalProductBodyException e) {
        log.error(e.getMessage(), e);
        AppResponse appResponse = new AppResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(appResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InapplicablePatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppResponse> handleInapplicablePatchException(InapplicablePatchException e) {
        log.error(e.getMessage(), e);
        AppResponse appResponse = new AppResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(appResponse, HttpStatus.BAD_REQUEST);
    }
}
