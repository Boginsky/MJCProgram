package com.epam.esm.web.link.impl;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.controller.UserController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserLinkAdder extends AbstractLinkAdder<UserDto> {

    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto entity) {
        Long id = entity.getUser().getId();
        addIdLink(CONTROLLER, entity, entity.getUser().getId(), SELF_LINK_NAME);
        entity.add(linkTo(CONTROLLER)
                .slash(id)
                .slash("?orders")
                .withRel("orders"));
        entity.add(linkTo(CONTROLLER)
                .slash(id)
                .slash("?gift-certificates")
                .withRel("gift-certificates"));
    }
}
