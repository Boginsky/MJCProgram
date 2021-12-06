package com.epam.esm.service.validator.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements Validator<Tag> {

    private static final int MAX_NAME_LENGTH = 300;
    private static final int MIN_NAME_LENGTH = 1;

    @Override
    public boolean isValid(Tag tag) {
        String tagName = tag.getName();
        int nameLength = tagName.length();
        return tagName != null &&
                nameLength >= MIN_NAME_LENGTH &&
                nameLength <= MAX_NAME_LENGTH;
    }
}
