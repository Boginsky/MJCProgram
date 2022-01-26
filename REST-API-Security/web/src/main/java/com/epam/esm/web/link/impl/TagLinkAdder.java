package com.epam.esm.web.link.impl;

import com.epam.esm.service.dto.entity.TagDto;
import com.epam.esm.web.controller.TagController;
import org.springframework.stereotype.Component;

@Component
public class TagLinkAdder extends AbstractLinkAdder<TagDto> {

    private static final Class<TagController> CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto entity) {
        Long id = entity.getId();
        addIdLinks(CONTROLLER, entity, id, SELF_LINK_NAME, DELETE_LINK_NAME, UPDATE_LINK_NAME);
    }
}
