package com.epam.esm.service.util.validator;

import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.service.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

@Component("tagValidator")
public class TagValidator extends AbstractValidator<TagDto> {

    protected TagValidator() {
        super(TagDto.class);
    }

    public void validateTagName(TagDto tagDto) {
        String name = tagDto.getName();
        if (name != null) {
            validateField(NAME, name);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }
}
