package com.epam.esm;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderRepository;
import com.epam.esm.model.repository.UserRepository;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.service.impl.OrderServiceImpl;
import com.epam.esm.service.util.validator.OrderValidator;
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
    private UserDto userDto;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;
    private Order order;
    private OrderDto orderDto;
    private Integer defaultPage;
    private Integer defaultPageSize;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private GiftCertificateRepository giftCertificateRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DtoConverter<Order, OrderDto> orderDtoConverter;

    @MockBean
    private OrderValidator orderValidator;

    @Autowired
    private OrderServiceImpl orderService;

    @Before
    public void setUp() {
        id = 1L;
        name = "name";
        defaultPage = 0;
        defaultPageSize = 10;
        user = User.builder()
                .id(id)
                .firstName(name)
                .lastName(name)
                .build();
        userDto = UserDto.builder()
                .id(id)
                .firstName(name)
                .lastName(name)
                .build();
        giftCertificate = GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(name)
                .price(BigDecimal.TEN)
                .duration(defaultPage)
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .build();
        giftCertificateDto = GiftCertificateDto.builder()
                .id(id)
                .name(name)
                .description(name)
                .price(BigDecimal.TEN)
                .duration(defaultPage)
                .createDate(ZonedDateTime.now())
                .lastUpdateDate(ZonedDateTime.now())
                .build();
        order = Order.builder()
                .id(id)
                .totalPrice(BigDecimal.TEN)
                .dateOfPurchase(ZonedDateTime.now())
                .user(user)
                .giftCertificate(giftCertificate)
                .build();
        orderDto = new OrderDto(id, giftCertificateDto, userDto, BigDecimal.ONE, ZonedDateTime.now());
        user.setOrderList(new HashSet<>());
    }

    @Test
    public void testCreateShouldCreateWhenNotExist() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        orderService.create(id, id);
        verify(orderRepository).save(any());
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowNoSuchEntityExceptionWhenUserNotFound() {
        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(giftCertificate));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        orderService.create(id, id);
    }

    @Test(expected = NoSuchEntityException.class)
    public void testCreateShouldThrowNoSuchEntityExceptionWhenGiftCertificateNotFound() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        orderService.create(id, id);
    }

    @Test
    public void testGetAllShouldGetAll() {
        orderService.getAll(defaultPage, defaultPageSize);
        verify(orderRepository).findAll();
    }

    @Test(expected = InvalidParametersException.class)
    public void testGetAllShouldThrowInvalidParametersExceptionWhenParametersInvalid() {
        orderService.getAll(-10, 10);
    }

    @Test
    public void testGetAllByUserIdShouldGetAll() {
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        orderService.getAllByUserId(id, defaultPage, defaultPageSize);
        verify(orderRepository).findAllByUserId(any(), any());
    }

    @Test(expected = NoSuchEntityException.class)
    public void testGetAllByUserIdShouldThrowNoSuchEntityExceptionWhenNotFound() {
        when(orderDtoConverter.convertToDto(any())).thenReturn(orderDto);
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        orderService.getAllByUserId(id, defaultPage, defaultPageSize);
        verify(orderRepository).findAllByUserId(any(), any());
    }

    @After
    public void tierDown() {
        id = null;
        name = null;
        user = null;
        userDto = null;
        giftCertificateDto = null;
        giftCertificate = null;
        order = null;
        orderDto = null;
        defaultPage = null;
        defaultPageSize = null;
    }
}
