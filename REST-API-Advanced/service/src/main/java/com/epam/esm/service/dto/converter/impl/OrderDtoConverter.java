package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoConverter implements DtoConverter<Order, OrderDto> {

    @Override
    public Order convertToEntity(OrderDto dto) {
        return Order.builder()
                .id(dto.getId())
                .giftCertificate(dto.getGiftCertificate())
                .totalPrice(dto.getTotalPrice())
                .dateOfPurchase(dto.getDateOfPurchase())
                .build();
    }

    @Override
    public OrderDto convertToDto(Order entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .giftCertificate(entity.getGiftCertificate())
                .totalPrice(entity.getTotalPrice())
                .dateOfPurchase(entity.getDateOfPurchase())
                .build();
    }
}
