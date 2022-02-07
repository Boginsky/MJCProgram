package com.epam.esm.web.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionResponse {

    private final String errorMessage;
    private final int errorCode;

}
