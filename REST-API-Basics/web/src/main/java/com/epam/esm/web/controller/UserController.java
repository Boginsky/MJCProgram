package com.epam.esm.web.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.service.UserService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LinkAdder<UserDto> userLinkAdder;

    @Autowired
    public UserController(UserService userService, LinkAdder<UserDto> userLinkAdder) {
        this.userService = userService;
        this.userLinkAdder = userLinkAdder;
    }

    @GetMapping(value = {"/{user-id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(
            @PathVariable(name = "user-id", required = false) Long userId,
            @RequestParam(name = "orders", required = false) String orders,
            @RequestParam(name = "gift-certificates", required = false) String giftCertificates,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size
    ) {
        List<UserDto> userDtoList = userService.getRoute(userId, orders, giftCertificates, page, size);
        return userDtoList.stream()
                .peek(userLinkAdder::addLinks)
                .collect(Collectors.toList());
    }
}
