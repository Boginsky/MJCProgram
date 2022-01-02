package com.epam.esm.web.link;

import org.springframework.hateoas.RepresentationModel;

public interface LinkAdder<T extends RepresentationModel<T>> {

    void addLinks(T entity);

}
