package com.schauzov.crudapp.exception.handling;

import com.schauzov.crudapp.exception.IllegalProductBodyException;
import com.schauzov.crudapp.exception.ProductNotFoundException;
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
    public ResponseEntity<AppError> handleProductNotFound(ProductNotFoundException e) {
        log.error(e.getMessage(), e);
        AppError appError = new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(appError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalProductBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<AppError> handleIllegalProductBodyExceptionn(IllegalProductBodyException e) {
        log.error(e.getMessage(), e);
        AppError appError = new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(appError, HttpStatus.BAD_REQUEST);
    }
}
