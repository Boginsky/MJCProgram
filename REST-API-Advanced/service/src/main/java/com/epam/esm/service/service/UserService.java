package com.epam.esm.service.service;

import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getRoute(Long userId, Integer page, Integer size);

    UserDto create(UserDto userDto);

    List<UserDto> getById(Long userId);

    List<UserDto> getAll(Integer page, Integer size);
}
