package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    @Override
    public User convertToEntity(UserDto dto) {
        return User.builder()
                .id(dto.getUser().getId())
                .firstName(dto.getUser().getFirstName())
                .lastName(dto.getUser().getLastName())
                .build();
    }

    @Override
    public UserDto convertToDto(User entity) {
        return UserDto.builder()
                .user(entity)
                .build();
    }
}
