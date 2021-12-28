package com.epam.esm.service.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final DtoConverter<User, UserDto> userDtoConverter;

    @Autowired
    public UserServiceImpl(UserDao userDao, OrderDao orderDao,
                           GiftCertificateDao giftCertificateDao,
                           DtoConverter<User, UserDto> userDtoConverter) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public List<UserDto> getRoute(Long userId, String orders,
                                  String giftCertificates, Integer page,
                                  Integer size) {
        if (orders != null && userId != null) {
            return getByIdWithOrders(userId);
        } else if (giftCertificates != null && userId != null) {
            return getByIdWithGiftCertificates(userId);
        } else if (userId != null) {
            return getById(userId);
        } else {
            return getAll(page, size);
        }
    }

    @Override
    public List<UserDto> getById(Long userId) {
        User user = isPresentUser(userId);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(UserDto.builder()
                .user(user)
                .build());
        return userDtoList;
    }

    @Override
    public List<UserDto> getByIdWithOrders(Long userId) {
        User user = isPresentUser(userId);
        List<Order> orderList = orderDao.getByUserId(userId);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(UserDto.builder()
                .user(user)
                .orderList(orderList)
                .build());
        return userDtoList;
    }

    @Override
    public List<UserDto> getByIdWithGiftCertificates(Long userId) {
        User user = isPresentUser(userId);
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAllByUserId(userId);
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(UserDto.builder()
                .user(user)
                .giftCertificateList(giftCertificateList)
                .build());
        return userDtoList;
    }

    @Override
    public List<UserDto> getAll(Integer page, Integer size) {
        Pageable pageable;
        try {
            pageable = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException("message.pagination.invalid");
        }
        List<User> userList = userDao.getAll(pageable).getContent();
        return userList.stream()
                .map(userDtoConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private User isPresentUser(Long id) {
        Optional<User> userOptional = userDao.getById(id);
        if (!userOptional.isPresent()) {
            throw new NoSuchEntityException("message.notFound");
        } else {
            return userOptional.get();
        }
    }
}

