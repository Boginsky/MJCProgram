package com.epam.esm.service.dto.converter.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.DtoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
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
        return GiftCertificateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .duration(entity.getDuration())
                .price(entity.getPrice())
                .createDate(entity.getCreateDate())
                .lastUpdateDate(entity.getLastUpdateDate())
                .certificateTags(new ArrayList<>())
                .build();
    }
}
