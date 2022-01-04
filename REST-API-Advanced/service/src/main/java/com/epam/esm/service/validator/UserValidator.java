package com.epam.esm.service.validator;

import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserValidator extends AbstractValidator<UserDto> {

    protected UserValidator() {
        super(UserDto.class);
    }

    public void validateLastName(UserDto userDto) {
        String lastName = userDto.getLastName();
        if (lastName != null) {
            validateField(LAST_NAME, lastName);
        }
    }

    public void validateFirstName(UserDto userDto) {
        String firstName = userDto.getFirstName();
        if (firstName != null) {
            validateField(FIRST_NAME, firstName);
        }
    }
}
