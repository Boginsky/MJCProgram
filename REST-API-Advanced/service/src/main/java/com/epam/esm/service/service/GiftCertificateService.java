package com.epam.esm.service.service;

import com.epam.esm.model.entity.CustomPage;
import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    CustomPage<GiftCertificateDto> getAll(Integer page, Integer size);

    CustomPage<GiftCertificateDto> getRoute(List<String> tagName, List<String> sortColumns,
                                      List<String> orderType, List<String> filterBy,
                                      Long giftCertificateId, Integer page, Integer size);

    void deleteById(Long id);

    CustomPage<GiftCertificateDto> getById(Long id);

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto);

    CustomPage<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy, Integer page, Integer size);

    CustomPage<GiftCertificateDto> getAllByTagName(List<String> tagName, Integer page, Integer size);

}
