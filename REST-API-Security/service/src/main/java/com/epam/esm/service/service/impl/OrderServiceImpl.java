package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.service.util.CommonUtil;
import com.epam.esm.service.util.validator.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;
    @Qualifier("orderDtoConverter")
    private final DtoConverter<Order, OrderDto> orderDtoConverter;
    @Qualifier("orderValidator")
    private final OrderValidator orderValidator;
    private final CommonUtil commonUtil;

    @Autowired
    public OrderServiceImpl(GiftCertificateRepository giftCertificateRepository, UserRepository userRepository,
                            OrderRepository orderRepository, DtoConverter<Order, OrderDto> orderDtoConverter,
                            OrderValidator orderValidator, CommonUtil commonUtil) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
        this.orderDtoConverter = orderDtoConverter;
        this.orderValidator = orderValidator;
        this.commonUtil = commonUtil;
    }

    @Override
    public OrderDto create(Long userId, Long giftCertificateId) {
        User user = isPresentUser(userId);
        GiftCertificate giftCertificate = isPresentGiftCertificate(giftCertificateId);
        checkOrder(user);
        Order order = Order.builder()
                .user(user)
                .giftCertificate(giftCertificate)
                .totalPrice(giftCertificate.getPrice())
                .dateOfPurchase(ZonedDateTime.now())
                .build();
        OrderDto orderDto = orderDtoConverter.convertToDto(order);
        orderValidator.validatePrice(orderDto);
        order = createOrder(order);
        return orderDtoConverter.convertToDto(order);
    }

    @Override
    public CustomPage<OrderDto> getRoute(Long orderId, Integer page, Integer size) {
        if (orderId != null) {
            return getById(orderId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public CustomPage<OrderDto> getRoute(Long userId, Long orderId, Integer page, Integer size) {
        if (userId != null && orderId != null) {
            return getOrderByUserId(userId, orderId);
        } else {
            return getAllByUserId(userId, page, size);
        }
    }

    @Override
    public CustomPage<OrderDto> getById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchEntityException("message.order.missing"));
        checkOrder(order);
        List<OrderDto> listOfOrders = new ArrayList<>();
        listOfOrders.add(orderDtoConverter.convertToDto(order));
        return CustomPage.<OrderDto>builder()
                .content(listOfOrders)
                .build();
    }

    @Override
    public CustomPage<OrderDto> getOrderByUserId(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoSuchEntityException("message.order.missing"));
        isUsers(order, userId);
        List<OrderDto> listOfOrders = new ArrayList<>();
        listOfOrders.add(orderDtoConverter.convertToDto(order));
        return CustomPage.<OrderDto>builder()
                .content(listOfOrders)
                .build();
    }

    @Override
    public CustomPage<OrderDto> getAll(Integer page, Integer size) {
        Pageable pageable = commonUtil.getPageable(page, size, Sort.unsorted());
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderDtoConverter.convertContentToDto(orderPage);
    }

    @Override
    public CustomPage<OrderDto> getAllByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = commonUtil.getPageable(page, size, Sort.unsorted());
        isPresentUser(userId);
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);
        return orderDtoConverter.convertContentToDto(orderPage);

    }

    private Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    private GiftCertificate isPresentGiftCertificate(Long giftCertificateId) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(giftCertificateId);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.certificate.missing");
        } else {
            return giftCertificateOptional.get();
        }
    }

    private User isPresentUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.user.missing");
        } else {
            return userOptional.get();
        }
    }

    private void checkOrder(User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if (!username.equals(user.getUsername())) {
            throw new InvalidParametersException("message.wrong.user.order");
        }
    }

    private void checkOrder(Order order) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if (!username.equals(order.getUser().getUsername())) {
            throw new InvalidParametersException("message.wrong.user.order");
        }
    }

    private void isUsers(Order order, Long userId) {
        if (!order.getUser().getId().equals(userId)) {
            throw new InvalidParametersException("message.order.missing");
        }
    }
}
