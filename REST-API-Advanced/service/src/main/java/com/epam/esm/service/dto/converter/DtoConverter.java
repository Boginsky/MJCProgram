package com.epam.esm.service.dto.converter;

public interface DtoConverter<S, D> {

    S convertToEntity(D dto);

    D convertToDto(S entity);
}
