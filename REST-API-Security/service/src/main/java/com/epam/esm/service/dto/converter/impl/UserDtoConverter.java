package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("userDtoConverter")
public class UserDtoConverter implements DtoConverter<User, UserDto> {

    @Override
    public User convertToEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .role(dto.getRole())
                .username(dto.getUsername())
                .status(dto.isStatus())
                .build();
    }

    @Override
    public UserDto convertToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .role(entity.getRole())
                .username(entity.getUsername())
                .status(entity.isStatus())
                .build();
    }

    public UserDto convertToDtoForOauth2(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .status(true)
                .role(Role.USER)
                .build();
    }

    @Override
    public CustomPage<UserDto> convertContentToDto(Page<User> customPage) {
        return CustomPage.<UserDto>builder()
                .currentPage(customPage.getNumber())
                .amountOfPages(customPage.getTotalPages())
                .firstPage(1)
                .lastPage(customPage.getTotalPages())
                .pageSize(customPage.getNumberOfElements())
                .content(customPage.getContent()
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
