package com.epam.esm.service.validator;

import com.epam.esm.service.exception.InvalidEntityException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public abstract class AbstractValidator<T> {

    protected static final String DURATION = "duration";
    protected static final String PRICE = "price";
    protected static final String DESCRIPTION = "description";
    protected static final String NAME = "name";
    protected static final String TOTAL_PRICE = "totalPrice";
    protected static final String FIRST_NAME = "firstName";
    protected static final String LAST_NAME = "lastName";

    protected final ValidatorFactory factory;
    protected final Validator validator;
    protected final Class<T> customClass;

    protected AbstractValidator(Class<T> customClass) {
        this.customClass = customClass;
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    void validateField(String propertyName, Object value) {
        Set<ConstraintViolation<T>> violations = validator.validateValue(customClass, propertyName, value);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new InvalidEntityException(message);
        }
    }
}
