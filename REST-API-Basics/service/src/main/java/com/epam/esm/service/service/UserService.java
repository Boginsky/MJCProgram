package com.epam.esm.service.service;

import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getRoute(Long userId, String orders, String giftCertificates,
                           Integer page, Integer size);

    List<UserDto> getById(Long userId);

    List<UserDto> getByIdWithOrders(Long userId);

    List<UserDto> getByIdWithGiftCertificates(Long userId);

    List<UserDto> getAll(Integer page, Integer size);
}
