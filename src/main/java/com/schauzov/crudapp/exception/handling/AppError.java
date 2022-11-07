package com.schauzov.crudapp.exception.handling;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppError {
    Integer errorCode;
    String errorMessage;
}
