package com.epam.esm.web.link.impl;

import com.epam.esm.web.link.LinkAdder;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class AbstractLinkAdder<T extends RepresentationModel<T>> implements LinkAdder<T> {

    protected static final String SELF_LINK_NAME = "self";
    protected static final String UPDATE_LINK_NAME = "update";
    protected static final String DELETE_LINK_NAME = "delete";
    protected static final String GET = "GET";
    protected static final String PATCH = "PATCH";
    protected static final String DELETE = "DELETE";

    protected void addIdLink(Class<?> controllerClass, T entity, Long id, String linkName) {
        switch (linkName) {
            case SELF_LINK_NAME:
                entity.add(linkTo(controllerClass).slash(id).withRel(linkName).withType(GET));
                break;
            case UPDATE_LINK_NAME:
                entity.add(linkTo(controllerClass).slash(id).withRel(linkName).withType(PATCH));
                break;
            case DELETE_LINK_NAME:
                entity.add(linkTo(controllerClass).slash(id).withRel(linkName).withType(DELETE));
                break;
            default:
                entity.add(linkTo(controllerClass).slash(id).withRel(linkName));
        }
    }

    protected void addIdLinks(Class<?> controllerClass, T entity, Long id, String... linkNames) {
        for (String linkName : linkNames) {
            addIdLink(controllerClass, entity, id, linkName);
        }
    }
}
