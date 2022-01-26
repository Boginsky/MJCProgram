package com.epam.esm.service.util.validator;

import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

@Component("userValidator")
public class UserValidator extends AbstractValidator<UserDto> {

    protected UserValidator() {
        super(UserDto.class);
    }

    public void validateLastName(UserDto userDto) {
        String lastName = userDto.getLastName();
        if (lastName != null) {
            validateField(LAST_NAME, lastName);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }

    public void validateFirstName(UserDto userDto) {
        String firstName = userDto.getFirstName();
        if (firstName != null) {
            validateField(FIRST_NAME, firstName);
        } else {
            throw new InvalidEntityException("message.validation.fail");
        }
    }
}
