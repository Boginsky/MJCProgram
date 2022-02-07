package com.epam.esm.web.link.impl;

import com.epam.esm.service.dto.entity.UserDto;
import com.epam.esm.web.controller.UserController;
import org.springframework.stereotype.Component;

@Component
public class UserLinkAdder extends AbstractLinkAdder<UserDto> {

    private static final Class<UserController> CONTROLLER = UserController.class;

    @Override
    public void addLinks(UserDto entity) {
        addIdLink(CONTROLLER, entity, entity.getId(), SELF_LINK_NAME);
    }
}
