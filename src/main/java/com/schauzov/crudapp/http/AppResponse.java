package com.schauzov.crudapp.http;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppResponse {
    Integer responseCode;
    String responseMessage;
}
