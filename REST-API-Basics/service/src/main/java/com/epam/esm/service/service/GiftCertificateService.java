package com.epam.esm.service.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    void create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificate> getAll();

    List<GiftCertificate> getRoute(Long giftCertificateId);

    List<GiftCertificateDto> getRouteWithTags(String tagName, List<String> sortColumns,
                                              List<String> orderType, List<String> filterBy);

    void deleteById(Long id);

    List<GiftCertificate> getById(Long id);

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getAllWithTags();

    List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy);

    List<GiftCertificateDto> getAllByTagName(String tagName);

}
