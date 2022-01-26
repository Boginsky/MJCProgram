package com.epam.esm.web.link.impl;

import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.OrderDto;
import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.link.LinkAdder;
import org.springframework.stereotype.Component;

@Component
public class OrderLinkAdder extends AbstractLinkAdder<OrderDto> {

    private static final Class<OrderController> CONTROLLER = OrderController.class;
    private final LinkAdder<UserDto> userDtoLinkAdder;
    private final LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder;

    public OrderLinkAdder(LinkAdder<UserDto> userDtoLinkAdder,
                          LinkAdder<GiftCertificateDto> giftCertificateDtoLinkAdder) {
        this.userDtoLinkAdder = userDtoLinkAdder;
        this.giftCertificateDtoLinkAdder = giftCertificateDtoLinkAdder;
    }

    @Override
    public void addLinks(OrderDto entity) {
        Long id = entity.getId();
        addIdLinks(CONTROLLER, entity, id, SELF_LINK_NAME);
        if (entity.getUser() != null) {
            userDtoLinkAdder.addLinks(entity.getUser());
        }
        if (entity.getGiftCertificate() != null) {
            giftCertificateDtoLinkAdder.addLinks(entity.getGiftCertificate());
        }
    }
}
