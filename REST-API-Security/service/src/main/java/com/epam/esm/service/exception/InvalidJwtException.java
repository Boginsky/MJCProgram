package com.epam.esm.service.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidJwtException(String msg) {
        super(msg);
    }
}
