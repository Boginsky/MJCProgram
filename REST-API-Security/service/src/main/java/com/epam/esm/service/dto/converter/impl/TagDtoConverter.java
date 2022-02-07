package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("tagDtoConverter")
public class TagDtoConverter implements DtoConverter<Tag, TagDto> {

    @Override
    public Tag convertToEntity(TagDto dto) {
        return Tag.builder()
                .id(dto.getId())
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

    @Override
    public CustomPage<TagDto> convertContentToDto(Page<Tag> customPage) {
        return CustomPage.<TagDto>builder()
                .currentPage(customPage.getNumber())
                .amountOfPages(customPage.getTotalPages())
                .firstPage(1)
                .lastPage(customPage.getTotalPages())
                .pageSize(customPage.getNumberOfElements())
                .content(customPage.getContent()
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
