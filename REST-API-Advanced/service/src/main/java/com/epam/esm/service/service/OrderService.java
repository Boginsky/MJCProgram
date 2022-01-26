package com.epam.esm.service.service;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.OrderDto;

public interface OrderService {

    OrderDto create(Long userId, Long giftCertificateId);

    CustomPage<OrderDto> getRoute(Long userId, Long orderId, Integer page, Integer size);

    CustomPage<OrderDto> getById(Long orderId);

    CustomPage<OrderDto> getAll(Integer page, Integer size);

    CustomPage<OrderDto> getAllByUserId(Long userId, Integer page, Integer size);

}
