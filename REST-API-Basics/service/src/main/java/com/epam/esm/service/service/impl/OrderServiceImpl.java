package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final UserDao userDao;
    private final DtoConverter<Order, OrderDto> orderDtoConverter;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, GiftCertificateDao giftCertificateDao,
                            UserDao userDao, DtoConverter<Order, OrderDto> orderDtoConverter) {
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.userDao = userDao;
        this.orderDtoConverter = orderDtoConverter;
    }

    @Override
    @Transactional
    public OrderDto create(Long userId, Long giftCertificateId) {
        User user = isPresentUser(userId);
        GiftCertificate giftCertificate = isPresentGiftCertificate(giftCertificateId);
        isDuplicate(userId, giftCertificateId);
        Order order = Order.builder()
                .user(user)
                .giftCertificate(giftCertificate)
                .totalPrice(giftCertificate.getPrice())
                .build();
        order = createOrder(order);
        return orderDtoConverter.convertToDto(order);
    }

    private Order createOrder(Order order) {
        Long id = orderDao.create(order);
        return isPresent(id);
    }

    private Order isPresent(Long id) {
        Optional<Order> orderOptional = orderDao.getById(id);
        if (!orderOptional.isPresent()) {
            throw new NoSuchEntityException("message.cantFindOrder");
        } else {
            return orderOptional.get();
        }
    }

    private void isDuplicate(Long userId, Long giftCertificateId) {
        Map<Long, Long> ordersMap = orderDao.getAllOrderIds();
        int size = ordersMap.size();
        ordersMap.put(giftCertificateId, userId);
        if (size == ordersMap.size()) {
            throw new DuplicateEntityException("message.orderExists");
        }
    }

    private GiftCertificate isPresentGiftCertificate(Long giftCertificateId) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.getById(giftCertificateId);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("message.notFound");
        } else {
            return giftCertificateOptional.get();
        }
    }

    private User isPresentUser(Long userId) {
        Optional<User> userOptional = userDao.getById(userId);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.cantFindUser");
        } else {
            return userOptional.get();
        }
    }

    @Override
    public List<OrderDto> getRoute(Long userId, Long orderId, Integer page, Integer size) {
        if (userId != null) {
            return getByUserId(userId);
        }else if(orderId != null){
            return getById(orderId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public List<OrderDto> getById(Long orderId) {
        Order order = orderDao.getById(orderId).orElseThrow(
                () -> new NoSuchEntityException("message.cantFindOrder"));
        List<OrderDto> listOfOrders = new ArrayList<>();
        listOfOrders.add(orderDtoConverter.convertToDto(order));
        return listOfOrders;
    }

    @Override
    public List<OrderDto> getAll(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<Order> orderList = orderDao.getAll(pageable).getContent();
        return orderList.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getByUserId(Long userId) {
        List<Order> orderList = orderDao.getByUserId(userId);
        return orderList.stream()
                .map(orderDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
