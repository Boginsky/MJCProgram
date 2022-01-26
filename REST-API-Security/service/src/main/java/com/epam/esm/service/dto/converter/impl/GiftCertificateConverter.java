package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.dto.converter.DtoConverter;
import com.epam.esm.service.dto.entity.GiftCertificateDto;
import com.epam.esm.service.dto.entity.TagDto;
import org.springframework.data.domain.Page;
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
        Set<TagDto> tagList = dto.getTags();
        Set<Tag> tagDtoList = new HashSet<>();
        for (TagDto tagDto : tagList) {
            tagDtoList.add(tagDtoConverter.convertToEntity(tagDto));
        }
        return GiftCertificate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .price(dto.getPrice())
                .createDate(dto.getCreateDate())
                .status(dto.isStatus())
                .lastUpdateDate(dto.getLastUpdateDate())
                .tags(tagDtoList)
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
                .status(entity.isStatus())
                .lastUpdateDate(entity.getLastUpdateDate())
                .tags(tagDtoList)
                .build();
    }

    @Override
    public CustomPage<GiftCertificateDto> convertContentToDto(Page<GiftCertificate> customPage) {
        return CustomPage.<GiftCertificateDto>builder()
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
