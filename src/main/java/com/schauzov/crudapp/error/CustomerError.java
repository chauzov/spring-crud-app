package com.schauzov.crudapp.error;

import lombok.Data;

@Data
public class CustomerError {
    Integer errorCode;
    String errorMessage;

    public CustomerError() {
    }

    public CustomerError(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
