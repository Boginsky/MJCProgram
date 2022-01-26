package com.epam.esm.service.service;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.entity.UserDto;

import java.util.Optional;

public interface UserService {

    CustomPage<UserDto> getRoute(Long userId, Integer page, Integer size);

    UserDto create(UserDto userDto);

    CustomPage<UserDto> getById(Long userId);

    CustomPage<UserDto> getAll(Integer page, Integer size);

    UserDto getByUsername(String username);

    void deleteById(Long id);

    Optional<User> getByEmail(String email);
}
