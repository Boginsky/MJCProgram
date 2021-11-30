package com.epam.esm.service.exception;

// FIXME: 01.12.2021 com.epam.esm.service.exception in model lay and maybe com.epam.esm.service.exception's name should be more detailed?
public class ServiceException extends Exception{

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
