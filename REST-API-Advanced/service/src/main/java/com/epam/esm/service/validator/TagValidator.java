package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDto;
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
        }
    }
}
