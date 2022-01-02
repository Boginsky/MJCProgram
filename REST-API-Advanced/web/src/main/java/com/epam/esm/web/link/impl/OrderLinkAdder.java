package com.epam.esm.web.link.impl;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.controller.OrderController;
import org.springframework.stereotype.Component;

@Component
public class OrderLinkAdder extends AbstractLinkAdder<OrderDto> {

    private static final Class<OrderController> CONTROLLER = OrderController.class;

    @Override
    public void addLinks(OrderDto entity) {
        Long id = entity.getId();
        addIdLinks(CONTROLLER, entity, id, SELF_LINK_NAME);
    }
}
