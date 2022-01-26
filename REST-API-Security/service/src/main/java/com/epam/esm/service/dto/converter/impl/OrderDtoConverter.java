package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.dto.entity.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("orderDtoConverter")
public class OrderDtoConverter implements DtoConverter<Order, OrderDto> {

    private final DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter;
    private final DtoConverter<User, UserDto> userDtoConverter;

    public OrderDtoConverter(DtoConverter<GiftCertificate, GiftCertificateDto> giftCertificateDtoConverter, DtoConverter<User, UserDto> userDtoConverter) {
        this.giftCertificateDtoConverter = giftCertificateDtoConverter;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public Order convertToEntity(OrderDto dto) {
        return Order.builder()
                .id(dto.getId())
                .giftCertificate(giftCertificateDtoConverter
                        .convertToEntity(dto.getGiftCertificate()))
                .totalPrice(dto.getTotalPrice())
                .user(userDtoConverter
                        .convertToEntity(dto.getUser()))
                .dateOfPurchase(dto.getDateOfPurchase())
                .build();
    }

    @Override
    public OrderDto convertToDto(Order entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .giftCertificate(giftCertificateDtoConverter
                        .convertToDto(entity.getGiftCertificate()))
                .totalPrice(entity.getTotalPrice())
                .user(userDtoConverter
                        .convertToDto(entity.getUser()))
                .dateOfPurchase(entity.getDateOfPurchase())
                .build();
    }

    @Override
    public CustomPage<OrderDto> convertContentToDto(Page<Order> customPage) {
        return CustomPage.<OrderDto>builder()
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
