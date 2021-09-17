package com.schauzov.crudapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalProductBodyException extends RuntimeException {
    public IllegalProductBodyException(String msg) {
        super(msg);
    }
}
