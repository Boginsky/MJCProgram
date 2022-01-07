package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component("giftCertificateConverter")
public class GiftCertificateConverter implements DtoConverter<GiftCertificate, GiftCertificateDto> {

    private final TagDtoConverter tagDtoConverter;

    public GiftCertificateConverter(TagDtoConverter tagDtoConverter) {
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    public GiftCertificate convertToEntity(GiftCertificateDto dto) {
        return GiftCertificate.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .build();
    }

    @Override
    public GiftCertificateDto convertToDto(GiftCertificate entity) {
        Set<Tag> tagList = entity.getTags();
        Set<TagDto> tagDtoList = new HashSet<>();
        for (Tag tag : tagList) {
            tagDtoList.add(tagDtoConverter.convertToDto(tag));
        }
        return GiftCertificateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .duration(entity.getDuration())
                .price(entity.getPrice())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .certificateTags(tagDtoList)
                .build();
    }

    @Override
    public CustomPage<GiftCertificateDto> convertContentToDto(CustomPage<GiftCertificate> customPage) {
        return CustomPage.<GiftCertificateDto>builder()
                .currentPage(customPage.getCurrentPage())
                .amountOfPages(customPage.getAmountOfPages())
                .firstPage(customPage.getFirstPage())
                .lastPage(customPage.getLastPage())
                .pageSize(customPage.getPageSize())
                .content(customPage.getContent()
                        .stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
