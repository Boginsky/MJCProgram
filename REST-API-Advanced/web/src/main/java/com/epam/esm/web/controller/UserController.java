package com.epam.esm.web.controller;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.service.service.UserService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final LinkAdder<UserDto> userLinkAdder;
    private final LinkAdder<OrderDto> orderLinkAdder;

    @Autowired
    public UserController(UserService userService, LinkAdder<UserDto> userLinkAdder,
                          OrderService orderService, LinkAdder<OrderDto> orderLinkAdder) {
        this.userService = userService;
        this.userLinkAdder = userLinkAdder;
        this.orderService = orderService;
        this.orderLinkAdder = orderLinkAdder;
    }

    @GetMapping(value = {"/{id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<UserDto> getAll(
            @PathVariable(name = "id", required = false) Long id,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        CustomPage<UserDto> userDtoList = userService.getRoute(id, page, size);
        userDtoList.getContent().forEach(userLinkAdder::addLinks);
        return userDtoList;
    }

    @PostMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@PathVariable(name = "id") Long userId,
                           @RequestParam(value = "gift-certificate-id") Long giftCertificateId
    ) {
        OrderDto orderDto = orderService.create(userId, giftCertificateId);
        orderLinkAdder.addLinks(orderDto);
        return orderDto;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.create(userDto);
    }
}
