package com.schauzov.crudapp.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchOperation {
    String op;
    String path;
    String from;
}
