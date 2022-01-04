package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.service.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;
    @Qualifier("orderDtoConverter")
    private final DtoConverter<Order, OrderDto> orderDtoConverter;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderServiceImpl(GiftCertificateRepository giftCertificateRepository, UserRepository userRepository,
                            OrderRepository orderRepository, DtoConverter<Order, OrderDto> orderDtoConverter,
                            OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
        this.orderDtoConverter = orderDtoConverter;
        this.orderValidator = orderValidator;
    }

    @Override
    @Transactional
    public OrderDto create(Long userId, Long giftCertificateId) {
        User user = isPresentUser(userId);
        GiftCertificate giftCertificate = isPresentGiftCertificate(giftCertificateId);
        Order order = Order.builder()
                .user(user)
                .giftCertificate(giftCertificate)
                .totalPrice(giftCertificate.getPrice())
                .build();
        OrderDto orderDto = orderDtoConverter.convertToDto(order);
        orderValidator.validatePrice(orderDto);
        order = createOrder(order);
        return orderDtoConverter.convertToDto(order);
    }

    @Override
    public List<OrderDto> getRoute(Long userId, Long orderId, Integer page, Integer size) {
        if (userId != null) {
            return getAllByUserId(userId, page, size);
        } else if (orderId != null) {
            return getById(orderId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public List<OrderDto> getById(Long orderId) {
        Order order = orderRepository.getByField("id", orderId).orElseThrow(
                () -> new NoSuchEntityException("message.order.missing"));
        List<OrderDto> listOfOrders = new ArrayList<>();
        listOfOrders.add(orderDtoConverter.convertToDto(order));
        return listOfOrders;
    }

    @Override
    public List<OrderDto> getAll(Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        List<Order> orderList = orderRepository.getAll(pageable);
        return orderList.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        isPresentUser(userId);
        List<Order> orderList = orderRepository.getAllByUserId(userId, pageable);
        return orderList.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private Pageable getPageable(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        return pageable;
    }


    private Order createOrder(Order order) {
        return orderRepository.create(order);
    }

    private GiftCertificate isPresentGiftCertificate(Long giftCertificateId) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getByField("id", giftCertificateId);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.certificate.missing");
        } else {
            return giftCertificateOptional.get();
        }
    }

    private User isPresentUser(Long userId) {
        Optional<User> userOptional = userRepository.getByField("id", userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        } else {
            return userOptional.get();
        }
    }
}
