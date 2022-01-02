package com.epam.esm.web.controller;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final LinkAdder<OrderDto> orderLinkAdder;

    @Autowired
    public OrderController(OrderService orderService, LinkAdder<OrderDto> orderLinkAdder) {
        this.orderService = orderService;
        this.orderLinkAdder = orderLinkAdder;
    }

    @PostMapping(value = "/{user-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@PathVariable(name = "user-id", required = false) Long userId,
                           @RequestParam(value = "gift-certificate-id") Long giftCertificateIds
    ) {
        OrderDto orderDto = orderService.create(userId, giftCertificateIds);
        orderLinkAdder.addLinks(orderDto);
        return orderDto;
    }

    @GetMapping(value = {"/{order-id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getAll(@PathVariable(name = "order-id", required = false) Long orderId,
                                 @RequestParam(name = "user-id", required = false) Long userId,
                                 @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                 @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        List<OrderDto> orderDtoList = orderService.getRoute(userId, orderId, page, size);
        orderDtoList.forEach(orderLinkAdder::addLinks);
        return orderDtoList;
    }
}
