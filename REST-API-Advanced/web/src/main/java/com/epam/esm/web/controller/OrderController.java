package com.epam.esm.web.controller;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.service.OrderService;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = {"/{id}", ""})
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<OrderDto> getAll(@PathVariable(name = "id", required = false) Long id,
                                       @RequestParam(name = "userId", required = false) Long userId,
                                       @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        CustomPage<OrderDto> orderDtoList = orderService.getRoute(userId, id, page, size);
        orderDtoList.getContent().forEach(orderLinkAdder::addLinks);
        return orderDtoList;
    }
}
