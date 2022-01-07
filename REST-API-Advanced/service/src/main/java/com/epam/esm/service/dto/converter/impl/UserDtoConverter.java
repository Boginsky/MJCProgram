package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    @Override
    public User convertToEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

    @Override
    public UserDto convertToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    @Override
    public CustomPage<UserDto> convertContentToDto(CustomPage<User> customPage) {
        return CustomPage.<UserDto>builder()
                .currentPage(customPage.getCurrentPage())
                .amountOfPages(customPage.getAmountOfPages())
                .firstPage(customPage.getFirstPage())
                .lastPage(customPage.getLastPage())
                .pageSize(customPage.getPageSize())
                .content(customPage.getContent()
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
