package com.epam.esm.service.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final UserRepository userRepository;
    private final DtoConverter<Order, OrderDto> orderDtoConverter;

    @Autowired
    public OrderServiceImpl(GiftCertificateRepository giftCertificateRepository, UserRepository userRepository,
                            OrderRepository orderRepository, DtoConverter<Order, OrderDto> orderDtoConverter) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
        this.orderDtoConverter = orderDtoConverter;
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
        validateFields(orderDto);
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
                () -> new NoSuchEntityException("message.cantFindOrder"));
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
        order = orderRepository.create(order);
        return isPresent(order.getId());
    }

    private Order isPresent(Long id) {
        Optional<Order> orderOptional = orderRepository.getByField("id", id);
        if (!orderOptional.isPresent()) {
            throw new NoSuchEntityException("message.cantFindOrder");
        } else {
            return orderOptional.get();
        }
    }

    private GiftCertificate isPresentGiftCertificate(Long giftCertificateId) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.getByField("id", giftCertificateId);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.notFound");
        } else {
            return giftCertificateOptional.get();
        }
    }

    private User isPresentUser(Long userId) {
        Optional<User> userOptional = userRepository.getByField("id", userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.cantFindUser");
        } else {
            return userOptional.get();
        }
    }

    private void validateFields(OrderDto orderDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        BigDecimal totalPrice = orderDto.getTotalPrice();
        if (totalPrice != null) {
            validateField(validator, "totalPrice", totalPrice);
        }
    }

    private void validateField(Validator validator, String propertyName, Object value) {
        Set<ConstraintViolation<OrderDto>> violations = validator.validateValue(
                OrderDto.class, propertyName, value);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new InvalidEntityException(message);
        }
    }
}
