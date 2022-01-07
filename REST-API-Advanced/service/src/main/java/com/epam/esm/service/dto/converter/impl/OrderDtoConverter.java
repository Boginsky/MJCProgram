package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("orderDtoConverter")
public class OrderDtoConverter implements DtoConverter<Order, OrderDto> {

    @Override
    public Order convertToEntity(OrderDto dto) {
        return Order.builder()
                .id(dto.getId())
                .giftCertificate(dto.getGiftCertificate())
                .totalPrice(dto.getTotalPrice())
                .user(dto.getUser())
                .dateOfPurchase(dto.getDateOfPurchase())
                .build();
    }

    @Override
    public OrderDto convertToDto(Order entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .giftCertificate(entity.getGiftCertificate())
                .totalPrice(entity.getTotalPrice())
                .user(entity.getUser())
                .dateOfPurchase(entity.getDateOfPurchase())
                .build();
    }

    @Override
    public CustomPage<OrderDto> convertContentToDto(CustomPage<Order> customPage) {
        return CustomPage.<OrderDto>builder()
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
