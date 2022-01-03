package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

@Component("tagDtoConverter")
public class TagDtoConverter implements DtoConverter<Tag, TagDto> {

    @Override
    public Tag convertToEntity(TagDto dto) {
        return Tag.builder()
                .name(dto.getName())
                .build();
    }

    @Override
    public TagDto convertToDto(Tag entity) {
        return TagDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
