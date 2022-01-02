package com.epam.esm.service.service;

import com.epam.esm.service.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto create(Long userId, Long giftCertificateId);

    List<OrderDto> getRoute(Long userId, Long orderId, Integer page, Integer size);

    List<OrderDto> getById(Long orderId);

    List<OrderDto> getAll(Integer page, Integer size);

    List<OrderDto> getAllByUserId(Long userId, Integer page, Integer size);

}
