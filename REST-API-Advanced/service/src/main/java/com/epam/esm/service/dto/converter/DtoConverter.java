package com.epam.esm.service.dto.converter;

import com.epam.esm.model.entity.CustomPage;

public interface DtoConverter<S, D> {

    S convertToEntity(D dto);

    D convertToDto(S entity);

    CustomPage<D> convertContentToDto(CustomPage<S> customPage);
}
