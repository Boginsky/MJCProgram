package com.epam.esm.service.service;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.UserDto;

public interface UserService {

    CustomPage<UserDto> getRoute(Long userId, Integer page, Integer size);

    UserDto create(UserDto userDto);

    CustomPage<UserDto> getById(Long userId);

    CustomPage<UserDto> getAll(Integer page, Integer size);

}
