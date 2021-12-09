package com.epam.esm.web.exception;

import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ExceptionControllerAdviser {

    private static final List<String> AVAILABLE_LOCALES = Arrays.asList("en_US", "ru_RU");
    private static final Locale DEFAULT_LOCALE = new Locale("en","US");

    private ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public ExceptionControllerAdviser(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(String message, Integer code, HttpStatus status){
        ExceptionResponse exceptionResponse = new ExceptionResponse(message,code);
        return new ResponseEntity<>(exceptionResponse,status);
    }

    private String resolveResourceBundle(String key, Locale locale){
        if(!AVAILABLE_LOCALES.contains(locale.toString())){
            locale = DEFAULT_LOCALE;
        }
        return bundleMessageSource.getMessage(key,null,locale);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(ServiceException e,Locale locale){
        return buildErrorResponse(resolveResourceBundle(e.getMessage(),locale),40001, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ExceptionResponse> handleControllerException(ControllerException e, Locale locale){
        return buildErrorResponse(resolveResourceBundle(e.getMessage(),locale),40001,HttpStatus.BAD_REQUEST);
    }
}
