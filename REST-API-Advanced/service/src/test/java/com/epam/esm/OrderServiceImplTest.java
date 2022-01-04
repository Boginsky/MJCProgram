package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.model.repository.impl.OrderRepositoryImpl;
import com.epam.esm.model.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.OrderServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {OrderServiceImpl.class})
public class OrderServiceImplTest {

    private Long id;
    private String name;
    private User user;
    private GiftCertificate giftCertificate;
    private Order order;
    private OrderDto orderDto;
    private Integer defaultPage;
    private Integer defaultPageSize;

    @MockBean
    private OrderRepositoryImpl orderRepository;

    @MockBean
    private GiftCertificateRepositoryImpl giftCertificateRepository;

    @MockBean
    private UserRepositoryImpl userRepository;

    @MockBean
    private DtoConverter<Order, OrderDto> orderDtoConverter;

    @Autowired
    private OrderServiceImpl orderService;

    @Before
    public void setUp() {
        id = 1L;
        name = "name";
        defaultPage = 0;
        defaultPageSize = 10;
        user = new User(id, name, name);
        giftCertificate = new GiftCertificate(id, name, name, BigDecimal.ZERO, defaultPage, ZonedDateTime.now(), ZonedDateTime.now());
        order = new Order(id, BigDecimal.ONE, ZonedDateTime.now(), user, giftCertificate);
        orderDto = new OrderDto(id, giftCertificate, user, BigDecimal.ONE, ZonedDateTime.now());
        user.setOrderList(new HashSet<>());
    }

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.of(user));
        when(giftCertificateRepository.getByField("id", id)).thenReturn(Optional.of(giftCertificate));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(orderRepository.getByField("id",id)).thenReturn(Optional.of(order));
        orderService.create(id, id);
        verify(orderRepository).create(any());
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowNoSuchEntityExceptionWhenUserNotFound() {
        when(giftCertificateRepository.getByField("id", id)).thenReturn(Optional.of(giftCertificate));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        orderService.create(id, id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowNoSuchEntityExceptionWhenGiftCertificateNotFound() {
        when(userRepository.getByField("id", id)).thenReturn(Optional.of(user));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        orderService.create(id, id);
    }

    @Test
    public void testGetAllShouldGetAll() {
        orderService.getAll(defaultPage, defaultPageSize);
        verify(orderRepository).getAll(any());
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParametersExceptionWhenParametersInvalid(){
        orderService.getAll(-10,10);
    }

    @Test
    public void testGetAllByUserIdShouldGetAll() {
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(userRepository.getByField("id",id)).thenReturn(Optional.of(user));
        orderService.getAllByUserId(id,defaultPage,defaultPageSize);
        verify(orderRepository).getAllByUserId(any(),any());
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetAllByUserIdShouldThrowNoSuchEntityExceptionWhenNotFound() {
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(userRepository.getByField("id",id)).thenReturn(Optional.empty());
        orderService.getAllByUserId(id,defaultPage,defaultPageSize);
        verify(orderRepository).getAllByUserId(any(),any());
    }

    @After
    public void tierDown() {
        id = null;
        name = null;
        user = null;
        giftCertificate = null;
        order = null;
        user = null;
        defaultPage = null;
        defaultPageSize = null;
    }
}
